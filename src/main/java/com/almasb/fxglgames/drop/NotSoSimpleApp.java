package com.almasb.fxglgames.drop;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.texture.GLImageView;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.run;

/**
 * @author Almas Baim (https://github.com/AlmasB)
 */
public class NotSoSimpleApp extends GameApplication {

    // shader from https://glslsandbox.com/e#109651.0

    private static final String SHADER = """
            #version 460
            
            uniform float time;
            uniform float resX;
            uniform float resY;
            
            vec3 lazer(vec2 pos, vec3 clr, float mult) {
                float x = time * 2.0;
                float w = fract(x * 0.5);
                w = sin(3.14156 * w);
                w *= 1.5 + pos.x;
                w *= 2.0;
                vec3 color = clr * mult * w / abs(pos.y);
            
                float d = distance(pos, vec2(-1.0 + fract(x * 0.5) * 2.0, 0.0));
                color += (clr * 0.25 * w / d);
                return color;
            }
            
            void main() {
                vec2 resolution = vec2(resX, resY);
                vec2 pos = ( gl_FragCoord.xy / resolution.xy * 2.0 ) - 1.0;
                vec3 color = max(vec3(0.0), lazer(pos, vec3(1.7, 0.2, 3.0), 0.25));
                gl_FragColor = vec4(color * 0.05, 1.0);
            }
            
            """;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
    }

    @Override
    protected void initGame() {
        GLImageView view = new GLImageView(1280, 720, SHADER);
        view.getProperties().setValue("resX", 1280.0);
        view.getProperties().setValue("resY", 720.0);
        view.getProperties().setValue("time", 0.0);

        run(() -> {
            view.getProperties().increment("time", +0.016);
        }, Duration.millis(16));

        entityBuilder()
                .at(0, 0)
                .view(view)
                .buildAndAttach();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
