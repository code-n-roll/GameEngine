package engineTester

import entities.Camera
import entities.Entity
import models.TexturedModel
import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.Vector3f
import renderEngine.DisplayManager
import renderEngine.Loader
import renderEngine.OBJLoader
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


        val model = OBJLoader.loadObjModel("stall", loader)

        val texture = ModelTexture(loader.loadTexture("stallTexture"))

        val staticModel = TexturedModel(model, texture)

        val entity = Entity(staticModel,
                Vector3f(0f, -5f, -50f),
                0f,
                0f,
                0f,
                1f)

        val camera = Camera()


        while (!Display.isCloseRequested()) {
            entity.increaseRotation(0f, 1f, 0f)
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
