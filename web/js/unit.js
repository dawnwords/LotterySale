/**
 * Created by Dawnwords on 2015/5/15.
 */
function Unit(treeElement, selectedElement) {
    var tree = $(treeElement).jstree({
        core: {
            animation: 0,
            check_callback: function (op, node) {
                console.log(node);
                return false;
            },
            themes: {stripes: true},
            data: {
                parentId: false,
                url: "../cunit",
                dataType: "json",
                type: 'post',
                contentType: "application/json",
                data: function (node) {
                    this.parentId = node.id;
                    return JSON.stringify(node.id === "#" ? null : {unitId: this.parentId});
                },
                success: function (nodes) {
                    if (nodes.length > 0) {
                        if (!nodes[0].state.opened) {
                            nodes.unshift({
                                text: "全选这一级",
                                children: false,
                                state: {opened: false, disabled: false, selected: false}
                            })
                        }
                    }
                }
            }
        },
        types: {
            '#': {icon: false},
            'default': {icon: false}
        },
        checkbox: {
            keep_selected_style: false,
            three_state: false
        },
        plugins: ['checkbox', 'types']
    });

    var unitSelected = $(selectedElement);

    var isSelectAll = function (node) {
        return isNaN(+node.id);
    };

    var getNodeTag = function (node) {
        return '<a class="btn btn-default tag" role="button">' + node.text
            + '&nbsp;<button type="button" class="close" aria-label="Close"><span aria-hidden="true" data-nodeid="'
            + node.id + '">&times;</span></button></a>';
    };

    var updateUnitSelected = function (nodes) {
        var html = "";
        for (var i in nodes) {
            var node = nodes[i];
            if (!isSelectAll(node)) {
                html += getNodeTag(node);
            }
        }
        unitSelected.html(html);
        unitSelected.find("span").click(function () {
            tree.uncheck_node(tree.get_node($(this).data("nodeid")));
        });
    };

    var deleteNode = function remove(nodes, toDelete) {
        for (var i = nodes.length; i--;) {
            if (nodes[i] === toDelete) {
                nodes.splice(i, 1);
            }
        }
    };

    var selectAndDeselectHandler = function (data, isSelect) {
        var tree = data.instance,
            selected = data.node,
            nodes = tree.get_selected(true);

        if (isSelectAll(selected)) {
            deleteNode(nodes, selected);
            var siblings = tree.get_node(selected.parent).children;
            for (var i in siblings) {
                var sibling = tree.get_node(siblings[i]);
                if (sibling !== selected) {
                    if (isSelect) {
                        tree.check_node(sibling);
                        nodes.push(sibling);
                    } else {
                        tree.uncheck_node(sibling);
                        deleteNode(nodes, sibling);
                    }
                }
            }
        }
        updateUnitSelected(nodes);
    };

    tree.bind("select_node.jstree", function (e, data) {
        selectAndDeselectHandler(data, true);
    });
    tree.bind("deselect_node.jstree", function (e, data) {
        selectAndDeselectHandler(data, false);
    });

    this.getSelectedIds = function () {
        return tree.jstree('get_selected');
    };

    this.getNodeById = function (id) {
        return tree.jstree('get_node', id);
    };

    this.getNodeLevelById = function (id) {
        return +tree.jstree('get_node', id, true).attr('aria-level');
    };

    this.deselectAll = function () {
        tree.jstree('deselect_all');
        unitSelected.html('');
    }
}