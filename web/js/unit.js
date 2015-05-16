/**
 * Created by Dawnwords on 2015/5/15.
 */
var treeOpt = {
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
};

var check_unit = function (e, data) {
    var tree = data.instance;
    var nodes = tree.get_selected(true);
    var html = "";
    var unit_selected = $("#unit-selected");
    for (var i in nodes) {
        var node = nodes[i];
        html += '<a class="btn btn-default tag" role="button">' + node.text
            + '<button type="button" class="close" aria-label="Close"><span aria-hidden="true" data-nodeid="'
            + node.id + '">&times;</span></button></a>';
    }
    unit_selected.html(html);
    unit_selected.find("span").click(function () {
        tree.uncheck_node(tree.get_node($(this).data("nodeid")));
    });
};


