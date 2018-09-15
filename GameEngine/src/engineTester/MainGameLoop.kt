package engineTester

import entities.Camera
import entities.Entity
import entities.Light
import models.TexturedModel
import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.Vector3f
import renderEngine.*
import shaders.StaticShader
import textures.ModelTexture

object MainGameLoop {

    @JvmStatic
    fun main(args: Array<String>) {

        DisplayManager.createDisplay()
        val loader = Loader()

        val model = OBJLoader.loadObjModel("stall", loader)

        val staticModel = TexturedModel(model, ModelTexture(loader.loadTexture("stallTexture")))
        val texture = staticModel.texture
        texture.shineDamper = 10f
        texture.reflectivity = 1f


        val entity = Entity(staticModel,
                Vector3f(0f, 0f, -25f),
                0f,
                160f,
                0f,
                1f)
        val light = Light(Vector3f(3000f,2000f,2000f), Vector3f(1f,1f,1f))

        val camera = Camera()

        val renderer = MasterRender()
        renderer.entities[staticModel] = arrayListOf(entity)
        while (!Display.isCloseRequested()) {
            entity.increaseRotation(0f, 0f, 0f)
            camera.move()
            renderer.processEntity(entity)
            renderer.render(light, camera)
            DisplayManager.updateDisplay()
        }

        renderer.cleanUp()
        DisplayManager.closeDisplay()
    }
}
