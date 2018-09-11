package entities

import org.lwjgl.input.Keyboard
import org.lwjgl.util.vector.Vector3f

class Camera {

    val position = Vector3f(0f, 0f, 0f)
    val pitch: Float = 0.toFloat()
    val yaw: Float = 0.toFloat()
    val roll: Float = 0.toFloat()

    fun move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z -= 0.02f
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += 0.02f
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x -= 0.02f
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z += 0.02f
        }
    }
}
