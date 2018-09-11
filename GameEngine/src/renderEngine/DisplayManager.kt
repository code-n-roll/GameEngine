package renderEngine

import org.lwjgl.opengl.*

object DisplayManager {

    private const val WIDTH = 1280
    private const val HEIGHT = 720
    private const val FPS_CAP = 120

    fun createDisplay() {

        val attribs = ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true)

        Display.setDisplayMode(DisplayMode(WIDTH, HEIGHT))
        Display.create(PixelFormat(), attribs)
        Display.setTitle("Our First Display")

        GL11.glViewport(0, 0, WIDTH, HEIGHT)
    }

    fun updateDisplay() {

        Display.sync(FPS_CAP)
        Display.update()
    }

    fun closeDisplay() {

        Display.destroy()
    }
}
