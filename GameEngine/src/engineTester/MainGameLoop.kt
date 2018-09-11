package engineTester

import entities.Camera
import entities.Entity
import models.TexturedModel
import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.Vector3f
import renderEngine.DisplayManager
import renderEngine.Loader
import renderEngine.Renderer
import shaders.StaticShader
import textures.ModelTexture

object MainGameLoop {

    @JvmStatic
    fun main(args: Array<String>) {

        DisplayManager.createDisplay()
        val loader = Loader()
        val shader = StaticShader()
        val renderer = Renderer(shader)

        val vertices = floatArrayOf(
                // to bottom of axis Z
                -0.5f, 0.5f, 0f, //V0
                -0.5f, -0.5f, 0f, //V1
                0.5f, -0.5f, 0f, //V2
                0.5f, 0.5f, 0f, //V3

                // to top of axis Z
                -0.5f, 0.5f, 1f, //V4
                -0.5f, -0.5f, 1f, //V5
                0.5f, -0.5f, 1f, //V6
                0.5f, 0.5f, 1f, //V7

                // to left of axis X
                -0.5f, 0.5f, 0f, //V8
                -0.5f, -0.5f, 0f, //V9
                -0.5f, -0.5f, 1f, //V10
                -0.5f, 0.5f, 1f, //V11

                // to right of axis X
                0.5f, 0.5f, 1f, //V12(7)
                0.5f, -0.5f, 1f, //V13(6)
                0.5f, -0.5f, 0f, //V14(2)
                0.5f, 0.5f, 0f, //V15(3)

                // to top of axis Y
                -0.5f, 0.5f, 0f, //V16(0)
                -0.5f, 0.5f, 1f, //V17(4)
                0.5f, 0.5f, 1f, //V18(7)
                0.5f, 0.5f, 0f, //V19(3)

                // to bottom of axis Y
                -0.5f, -0.5f, 1f, //V20(5)
                -0.5f, -0.5f, 0f, //V21(1)
                0.5f, -0.5f, 0f, //V22(2)
                0.5f, -0.5f, 1f)//V23(6)

        val indices = intArrayOf(
                // to bottom of axis Z
                0, 1, 3, // Top left bottom triangle (V0,V1,V3)
                3, 1, 2, // Bottom right triangle (V3,V1,V2)

                // to top of axis Z
                4, 5, 7, 7, 5, 6,

                // to left of axis X
                8, 9, 11, 11, 9, 10,

                // to right of axis X
                12, 13, 15, 15, 13, 14,

                // to top of axis Y
                16, 17, 19, 19, 17, 18,


                // to bottom of axis Y
                20, 21, 23, 23, 21, 22)

        val textureCoords = floatArrayOf(

                //1
                0f, 0f, //V0
                0f, 1f, //V1
                1f, 1f, //V2
                1f, 0f, //V3

                //2
                0f, 0f, //V0
                0f, 1f, //V1
                1f, 1f, //V2
                1f, 0f, //V3

                //3
                0f, 0f, //V0
                0f, 1f, //V1
                1f, 1f, //V2
                1f, 0f, //V3

                //4
                0f, 0f, //V0
                0f, 1f, //V1
                1f, 1f, //V2
                1f, 0f, //V3

                //5
                0f, 0f, //V0
                0f, 1f, //V1
                1f, 1f, //V2
                1f, 0f, //V3

                //6
                0f, 0f, //V0
                0f, 1f, //V1
                1f, 1f, //V2
                1f, 0f)//V3


        val model = loader.loadToVAO(vertices, indices, textureCoords)

        val texture = ModelTexture(loader.loadTexture("image.png"))

        val staticModel = TexturedModel(model, texture)

        val entity = Entity(staticModel,
                Vector3f(0f, 0f, -5f),
                0f,
                0f,
                0f,
                1f)

        val camera = Camera()


        while (!Display.isCloseRequested()) {
            entity.increaseRotation(1f, 1f, 0f)
            camera.move()
            renderer.prepare()
            shader.start()
            shader.loadViewMatrix(camera)
            renderer.render(entity, shader)
            shader.stop()
            DisplayManager.updateDisplay()

        }

        shader.cleanUp()
        loader.cleanUp()
        DisplayManager.closeDisplay()
    }
}
