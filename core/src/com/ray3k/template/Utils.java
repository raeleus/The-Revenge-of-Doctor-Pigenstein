package com.ray3k.template;

import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;
import com.esotericsoftware.spine.SkeletonBounds;

public class Utils {
    public static Array<Actor> getActorsRecursive(Actor actor) {
        Array<Actor> actors = new Array<>();
        if (actor instanceof Group) {
            actors.addAll(((Group) actor).getChildren());
            
            for (int i = 0; i < ((Group) actor).getChildren().size; i++) {
                Actor child = ((Group) actor).getChild(i);
                Array<Actor> newActors = getActorsRecursive(child);
                actors.addAll(newActors);
            }
        }
        
        return actors;
    }
    
    private static EarClippingTriangulator earClippingTriangulator = new EarClippingTriangulator();
    private static FloatArray floatArray = new FloatArray();
    
    public static float[] skeletonBoundsToTriangles(SkeletonBounds skeletonBounds) {
        floatArray.clear();
        
        for (FloatArray points : skeletonBounds.getPolygons()) {
            ShortArray shortArray = earClippingTriangulator.computeTriangles(points);
            for (int i = 0; i < shortArray.size; i++) {
                floatArray.add(shortArray.get(i));
            }
        }
        return floatArray.items;
    }
}
