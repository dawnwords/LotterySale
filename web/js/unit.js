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
                url: "../cunit",
                dataType: "json",
                type: 'post',
                contentType: "application/json",
                data: function (node) {
                    return JSON.stringify(node.id === "#" ? null : {unitId: node.id});
                }
            }
        },
        types: {
            '#': {icon: false},
            'default': {icon: false}
        },
        checkbox: {
            keep_selected_style: false
        },
        plugins: ['checkbox', 'types']
    });

    var unitSelected = $(selectedElement);

    tree.bind("changed.jstree", function (e, data) {
        var tree = data.instance;
        var nodes = tree.get_selected(true);
        var html = "";
        for (var i in nodes) {
            var node = nodes[i];
            html += '<a class="btn btn-default tag" role="button">' + node.text
                + '&nbsp;<button type="button" class="close" aria-label="Close"><span aria-hidden="true" data-nodeid="'
                + node.id + '">&times;</span></button></a>';
        }
        unitSelected.html(html);
        unitSelected.find("span").click(function () {
            tree.uncheck_node(tree.get_node($(this).data("nodeid")));
        });
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