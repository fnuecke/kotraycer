import li.cil.kotraycer.geometry.ShapeList
import li.cil.kotraycer.geometry.Sphere
import li.cil.kotraycer.material.*
import li.cil.kotraycer.math.*
import li.cil.kotraycer.render.MAX_RECURSION
import li.cil.kotraycer.render.RenderContext
import li.cil.kotraycer.render.SimpleCamera
import li.cil.kotraycer.render.trace
import java.awt.Desktop
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.random.Random

const val SAMPLE_COUNT = 100

fun main() {
    val resolutionX = 512
    val resolutionY = 512

    val camera = SimpleCamera(
        Vector3(0f, 1f, -1.2f),
        Vector3(0f, 0.5f, 0f),
        Vector2(resolutionX.toFloat(), resolutionY.toFloat())
    )

    val scene = ShapeList(
        Sphere(Vector3(0f, -3000f, 0f), 3000f, Lambertian(Color(0.7f, 0.7f, 0.7f))), // Ground
        Sphere(Vector3(0f, 0.5f, 1f), 0.5f, Lambertian(Color(1f, 0f, 0f))),
        Sphere(Vector3(1f, .8f, 2.2f), 1f, Metallic(Color(0f, 1f, 0f), 0.7f)),
        Sphere(Vector3(-1.2f, .5f, 1.4f), 0.8f, Metallic(Color(0f, 0f, 1f), 0.1f)),
        Sphere(Vector3(0.3f, -0.2f, .2f), 0.6f, Emissive(Color(0.95f, 0.9f, 0.9f))),
        Sphere(Vector3(-0.2f, .9f, -0.1f), 0.25f, Dielectric(1.7f)),
        Sphere(Vector3(-1.2f, -0.3f, .5f), 0.8f, Dielectric(1.2f)),
    )

    val image = BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_RGB)

    val context = RenderContext(scene, Random(0xdeadbeef), MAX_RECURSION)
    val samples = BlueNoise.generate(context.rng, SAMPLE_COUNT)

    for (y in 0 until resolutionY) {
        for (x in 0 until resolutionX) {
            var color = Colors.BLACK
            for (sample in samples) {
                val uv = Vector2(
                    (x + sample.x) / (resolutionX - 1f),
                    (y + sample.y) / (resolutionY - 1f)
                )
                val ray = camera.getRay(uv)
                color += trace(ray, context)
            }
            image.setRGB(x, resolutionY - 1 - y, Colors.toRGBInt(color / samples.size.toFloat()))
        }
    }

    val file = File("render.png")
    ImageIO.write(image, "png", file)
    Desktop.getDesktop().open(file)
}

