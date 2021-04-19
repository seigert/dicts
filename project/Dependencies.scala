import sbt.{ Def, _ }

object Dependencies {

  object Version {
    val Scala            = "2.13.5"
    val BetterMonadicFor = "0.3.1"
    val KindProjector    = "0.10.3"
    val SemanticDB       = "4.4.13"

    val Jmh = "1.25"

    val Weaver = "0.7.1"
  }

  val root: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq.empty)

  val benchmark = Def.setting(
    Seq(
      "org.openjdk.jmh" % "jmh-core"                 % Version.Jmh,
      "org.openjdk.jmh" % "jmh-generator-annprocess" % Version.Jmh
    )
  )

  val tests = Def.setting(
    Seq(
      "com.disneystreaming" %% "weaver-cats"       % Version.Weaver,
      "com.disneystreaming" %% "weaver-scalacheck" % Version.Weaver,
      "com.disneystreaming" %% "weaver-discipline" % Version.Weaver,
    ).map(_ % Test)
  )
}
