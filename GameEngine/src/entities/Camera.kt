package entities

import org.lwjgl.input.Keyboard
import org.lwjgl.util.vector.Vector3f

class Camera {

    companion object {
        private const val step = 1f
    }

    val position = Vector3f(0f, 10f, 0f)
    var pitch: Float = 0f
    var yaw: Float = 100f
    var roll: Float = 0f

    fun move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            val alpha = Math.toRadians(yaw.toDouble())
            System.out.println("z: ${position.z}\n" +
                            "x: ${position.x}\n" +
                            "sin a: ${Math.sin(alpha)}\n" +
                            "cos a: ${Math.cos(alpha)}")
            position.z -= step * Math.cos(alpha).toFloat()
            position.x += step * Math.sin(alpha).toFloat()
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
//            position.x -= step
            val alpha = Math.toRadians(yaw.toDouble())
            position.z += step * Math.sin(alpha).toFloat()
            position.x += step * Math.cos(alpha).toFloat()
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
//            position.x += step
            val alpha = Math.toRadians(yaw.toDouble())
            position.z -= step * Math.sin(alpha).toFloat()
            position.x -= step * Math.cos(alpha).toFloat()
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            System.out.println(
                    "z: ${position.z}\n" +
                            "x: ${position.x}")
            val alpha = Math.toRadians(yaw.toDouble())
            position.z += step * Math.cos(alpha).toFloat()
            position.x -= step * Math.sin(alpha).toFloat()
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.y += 0.2f
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            position.y -= 0.2f
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            yaw -= 1f
            System.out.println("yaw: $yaw\n")
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            yaw += 1f
            System.out.println("yaw: $yaw\n")
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            pitch += 1f
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            pitch -= 1f
        }
    }
}
