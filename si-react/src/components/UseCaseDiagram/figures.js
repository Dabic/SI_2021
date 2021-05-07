import * as go from "gojs";
var KAPPA = 4 * ((Math.sqrt(2) - 1) / 3);
go.Shape.defineFigureGenerator("Actor", function(shape, w, h) {
    var geo = new go.Geometry();
    var radiusw = .2;
    var radiush = .1;
    var offsetw = KAPPA * radiusw;
    var offseth = KAPPA * radiush;
    var centerx = .5;
    var centery = .1;
    var fig = new go.PathFigure(centerx * w, (centery + radiush) * h, true);
    geo.add(fig);

    // Head
    fig.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - radiusw) * w, centery * h, (centerx - offsetw) * w, (centery + radiush) * h,
        (centerx - radiusw) * w, (centery + offseth) * h));
    fig.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - radiush) * h, (centerx - radiusw) * w, (centery - offseth) * h,
        (centerx - offsetw) * w, (centery - radiush) * h));
    fig.add(new go.PathSegment(go.PathSegment.Bezier, (centerx + radiusw) * w, centery * h, (centerx + offsetw) * w, (centery - radiush) * h,
        (centerx + radiusw) * w, (centery - offseth) * h));
    fig.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery + radiush) * h, (centerx + radiusw) * w, (centery + offseth) * h,
        (centerx + offsetw) * w, (centery + radiush) * h));
    var r = .05;
    var cpOffset = KAPPA * r;
    centerx = .05;
    centery = .25;
    var fig2 = new go.PathFigure(.5 * w, .2 * h, true);
    geo.add(fig2);
    // Body
    fig2.add(new go.PathSegment(go.PathSegment.Line, .95 * w, .2 * h));
    centerx = .95;
    centery = .25;
    // Right arm
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx + r) * w, centery * h, (centerx + cpOffset) * w, (centery - r) * h,
        (centerx + r) * w, (centery - cpOffset) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, w, .6 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .85 * w, .6 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .85 * w, .35 * h));
    r = .025;
    cpOffset = KAPPA * r;
    centerx = .825;
    centery = .35;
    // Right under arm
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - r) * h, (centerx + r) * w, (centery - cpOffset) * h,
        (centerx + cpOffset) * w, (centery - r) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - r) * w, centery * h, (centerx - cpOffset) * w, (centery - r) * h,
        (centerx - r) * w, (centery - cpOffset) * h));
    // Right side/leg
    fig2.add(new go.PathSegment(go.PathSegment.Line, .8 * w, h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .55 * w, h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .55 * w, .7 * h));
    // Right in between
    r = .05;
    cpOffset = KAPPA * r;
    centerx = .5;
    centery = .7;
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - r) * h, (centerx + r) * w, (centery - cpOffset) * h,
        (centerx + cpOffset) * w, (centery - r) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - r) * w, centery * h, (centerx - cpOffset) * w, (centery - r) * h,
        (centerx - r) * w, (centery - cpOffset) * h));
    // Left side/leg
    fig2.add(new go.PathSegment(go.PathSegment.Line, .45 * w, h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .2 * w, h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .2 * w, .35 * h));
    r = .025;
    cpOffset = KAPPA * r;
    centerx = .175;
    centery = .35;
    // Left under arm
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - r) * h, (centerx + r) * w, (centery - cpOffset) * h,
        (centerx + cpOffset) * w, (centery - r) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - r) * w, centery * h, (centerx - cpOffset) * w, (centery - r) * h,
        (centerx - r) * w, (centery - cpOffset) * h));
    // Left arm
    fig2.add(new go.PathSegment(go.PathSegment.Line, .15 * w, .6 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, 0, .6 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, 0, .25 * h));
    r = .05;
    cpOffset = KAPPA * r;
    centerx = .05;
    centery = .25;
    // Left shoulder
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - r) * h, (centerx - r) * w, (centery - cpOffset) * h,
        (centerx - cpOffset) * w, (centery - r) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .5 * w, .2 * h));
    geo.spot1 = new go.Spot(.2, .2);
    geo.spot2 = new go.Spot(.8, .65);
    return geo;
});