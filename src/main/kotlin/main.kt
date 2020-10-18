import li.cil.kotraycer.geometry.AABB
import li.cil.kotraycer.geometry.BVH
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
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.min
import kotlin.random.Random
import kotlin.system.measureTimeMillis

const val SAMPLE_COUNT = 100

fun main() {
    val resolutionX = 512
    val resolutionY = 512

    val camera = SimpleCamera(
        Vector3(0f, 3f, -12f),
        Vector3(0f, 1f, 0f),
        Vector2(resolutionX.toFloat(), resolutionY.toFloat())
    )

    val colors = listOf(
        Color(0.9f, 0.2f, 0.2f),
        Color(0.2f, 1f, 0.3f),
        Color(0.3f, 0.5f, 1f),
        Color(1f, 0.9f, 0.2f),
        Color(0.2f, 0.7f, 0.9f),
    )

    fun Random.nextLambertian() = Lambertian(colors[nextInt(colors.size)])
    fun Random.nextMetallic() = Metallic(colors[nextInt(colors.size)], nextFloat())
    fun Random.nextDielectric() = Dielectric(1 + nextFloat())
    fun Random.nextEmissive() = Emissive(Colors.WHITE.lerp(colors[nextInt(colors.size)], nextFloat() * 0.2f))

    val materialPool = listOf(
        { rng: Random -> rng.nextLambertian() },
        { rng: Random -> rng.nextLambertian() },
        { rng: Random -> rng.nextLambertian() },
        { rng: Random -> rng.nextLambertian() },
        { rng: Random -> rng.nextLambertian() },
        { rng: Random -> rng.nextMetallic() },
        { rng: Random -> rng.nextMetallic() },
        { rng: Random -> rng.nextDielectric() },
        { rng: Random -> rng.nextEmissive() },
    )

    val rng = Random(0xdeadbeef)
    val area = AABB(Vector3(-10f, 0f, -10f), Vector3(10f, 5f, 10f))
    val spheres = BVH((0..200).map {
        val position = (rng.nextVector3() - 0.5f) * area.size()
        val material = materialPool[rng.nextInt(materialPool.size)](rng)
        Sphere(position, 0.2f + rng.nextFloat() * 0.7f, material)
    })

    val scene = ShapeList(
        listOf(
            Sphere(Vector3(0f, -3000f, 0f), 3000f, Lambertian(Color(0.7f, 0.7f, 0.7f))), // Ground
            spheres
//            Sphere(Vector3(0f, 0.5f, 1f), 0.5f, Lambertian(Color(1f, 0f, 0f))),
//            Sphere(Vector3(1f, .8f, 2.2f), 1f, Metallic(Color(0f, 1f, 0f), 0.7f)),
//            Sphere(Vector3(-1.2f, .5f, 1.4f), 0.8f, Metallic(Color(0f, 0f, 1f), 0.1f)),
//            Sphere(Vector3(0.3f, -0.2f, .2f), 0.6f, Emissive(Color(0.95f, 0.9f, 0.9f))),
//            Sphere(Vector3(-0.2f, .9f, -0.1f), 0.25f, Dielectric(1.7f)),
//            Sphere(Vector3(-1.2f, -0.3f, .5f), 0.8f, Dielectric(1.2f)),
        )
    )

    val image = BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_RGB)
    val samples = BlueNoise.generate(rng, SAMPLE_COUNT)

    val jobSize = 16
    val jobs = (0 until resolutionX step jobSize).flatMap { x ->
        (0 until resolutionY step jobSize).map { y -> Pair(x, y) }
    }

    val totalTime = measureTimeMillis {
        jobs.parallelStream().forEach { (x0, y0) ->
            val context = RenderContext(scene, Random(Objects.hash(x0, y0)), MAX_RECURSION)
            (y0 until min(y0 + jobSize, resolutionY)).forEach { y ->
                (x0 until min(x0 + jobSize, resolutionX)).forEach { x ->
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
        }
    }

    println("took %.2fs".format(totalTime / 1000f))

    val file = File("render.png")
    ImageIO.write(image, "png", file)
    Desktop.getDesktop().open(file)
}
