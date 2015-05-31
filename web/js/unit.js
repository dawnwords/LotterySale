/**
 * Created by Dawnwords on 2015/5/15.
 */
function Unit(treeElement, selectedElement, searchElement) {
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
                                text: "全选",
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

    var search = $(searchElement).typeahead({
        source: function (query, process) {
            var data = JSON.stringify({
                keyword: query,
                count: 8
            });
            console.log(data);
            $.ajax({
                url: "../sunit",
                dataType: "json",
                type: 'post',
                contentType: "application/json",
                data: data,
                success: function (data) {
                    if (data.length > 0) {
                        process(data);
                    }
                }
            });
        },
        autoSelect: true,
        afterSelect: function (data) {
            tree.jstree('check_node', tree.jstree('get_node', data.id));
            search.val("");
        }
    });

    var unitSelected = $(selectedElement);

    var isSelectAll = function (node) {
        return isNaN(+((node.id === undefined) ? node : node.id));
    };

    var getNodeTag = function (node) {
        return '<div class="btn btn-default tag" role="button">' + node.text
            + '&nbsp;<button type="button" class="close" aria-label="Close" data-nodeid="'
            + node.id + '"><span aria-hidden="true" >&times;</span></button></div>';
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
        unitSelected.find("button").click(function () {
            tree.jstree('uncheck_node', tree.jstree('get_node', $(this).data("nodeid")));
        });
    };

    var deleteNode = function (nodes, toDelete) {
        for (var i = nodes.length; i--;) {
            if (nodes[i] === toDelete) {
                nodes.splice(i, 1);
            }
        }
    };

    var pushNodeWithoutRedundancy = function (nodes, target) {
        for (var i = nodes.length; i--;) {
            if (nodes[i] === target) {
                return;
            }
        }
        nodes.push(nodes, target);
    };

    var selectAndDeselectHandler = function (data, isSelect) {
        var tree = data.instance,
            selected = data.node,
            nodes = tree.get_selected(true),
            siblings = tree.get_node(selected.parent).children,
            sibling, i;

        if (isSelectAll(selected)) {
            deleteNode(nodes, selected);
            for (i in siblings) {
                sibling = tree.get_node(siblings[i]);
                if (sibling !== selected) {
                    if (isSelect) {
                        tree.check_node(sibling);
                        pushNodeWithoutRedundancy(nodes, sibling);
                    } else {
                        tree.uncheck_node(sibling);
                        deleteNode(nodes, sibling);
                    }
                }
            }
        } else if (!isSelect) {
            var selectAllNode, notAllSelected = false;
            for (i in siblings) {
                sibling = tree.get_node(siblings[i]);
                if (isSelectAll(sibling)) {
                    selectAllNode = sibling;
                } else if (tree.is_checked(sibling)) {
                    notAllSelected = true;
                }
            }
            if (!notAllSelected) {
                tree.uncheck_node(selectAllNode);
                deleteNode(nodes, selectAllNode);
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
        var result = tree.jstree('get_selected');
        for (var i = result.length; i--;) {
            if (isSelectAll(result[i])) {
                result.splice(i, 1);
            }
        }
        return result;
    };

    this.getNodeById = function (id) {
        return tree.jstree('get_node', id);
    };

    this.deselectAll = function () {
        tree.jstree('deselect_all');
        unitSelected.html('');
    }
}