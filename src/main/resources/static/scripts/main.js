$(function () {

    var body = $("body");

    // get md
    var mdContent = body.text();

    // empty md
    body.empty();

    // translate in html
    body.html(markdown.toHTML(mdContent));
});
