package engineTester

import entities.Camera
import entities.Entity
import entities.Light
import models.TexturedModel
import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.Vector3f
import renderEngine.*
import terrains.Terrain
import textures.ModelTexture
import java.util.*

object MainGameLoop {

    @JvmStatic
    fun main(args: Array<String>) {

        DisplayManager.createDisplay()
        val loader = Loader()

        val model = OBJLoader.loadObjModel("tree", loader)
        val staticModel = TexturedModel(model, ModelTexture(loader.loadTexture("tree")))

//        val texture = staticModel.texture
//        texture.shineDamper = 10f
//        texture.reflectivity = 1f
//
//        val entity = Entity(staticModel,
//                Vector3f(0f, 0f, -25f),
//                0f,
//                160f,
//                0f,
//                1f)
        val entities = arrayListOf<Entity>()
        val random = Random()
        repeat(500) {
            entities.add(Entity(staticModel, Vector3f(
                    random.nextFloat() * 800f,
                    0f,
                    random.nextFloat() * 600f
            ),
                    0f,
                    0f,
                    0f,
                    3f))
        }
        val light = Light(Vector3f(20000f,20000f,2000f), Vector3f(1f,1f,1f))

        val terrain = Terrain(0f, 0f, loader, ModelTexture(loader.loadTexture("grass")))
        val terrain2 = Terrain(1f, 0f, loader, ModelTexture(loader.loadTexture("grass")))


        val camera = Camera()
        val renderer = MasterRenderer()

//        renderer.entities[staticModel] = arrayListOf(entity)
        while (!Display.isCloseRequested()) {
//            entity.increaseRotation(0f, 0f, 0f)
            camera.move()

            renderer.processTerrain(terrain)
            renderer.processTerrain(terrain2)
            for (entity in entities) {
                renderer.processEntity(entity)
            }

            renderer.render(light, camera)
            DisplayManager.updateDisplay()
        }

        renderer.cleanUp()
        loader.cleanUp()
        DisplayManager.closeDisplay()
    }
}
