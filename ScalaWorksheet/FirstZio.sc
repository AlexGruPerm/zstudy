import zio.console.Console
import zio.{App, ExitCode, Has, Runtime, UIO, URIO, ZEnv, ZIO, ZLayer}

val z : URIO[Int,Int] = URIO.fromFunction(i => i*10)

val prg :ZIO[Console with Has[Int],Throwable,Int] = for {
  prm <- ZIO.environment[Has[Int]].map(hi => hi.get)
  res <- z.provide(prm)
  _ <- zio.console.putStrLn(res.toString)
} yield res

object MyApp extends App {
  override def run(args: List[String]) :ZIO[ZEnv,Nothing,ExitCode] = {
    prg.provideLayer(Console.live ++ ZLayer.fromEffect(UIO(5))).exitCode
  }
}

val runtime = Runtime.default
runtime.unsafeRun(MyApp.run(List()))