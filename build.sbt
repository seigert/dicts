addCommandAlias("fix", "all compile:scalafix test:scalafix")
addCommandAlias("lint", "compile:scalafix --check ; test:scalafix --check")

inThisBuild(
  Seq(
    organization := "com.github.seigert",
    resolvers ++= Seq(
      Resolver.defaultLocal,
      Resolver.sonatypeRepo("releases")
    ),
    scalaVersion := Dependencies.Version.Scala,
    autoAPIMappings := true, // will use external ScalaDoc links for managed dependencies
    updateOptions := updateOptions.value.withCachedResolution(true),
    turbo := true,
    addCompilerPlugin(scalafixSemanticdb(Dependencies.Version.SemanticDB)),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % Dependencies.Version.BetterMonadicFor),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % Dependencies.Version.KindProjector),
  )
)

lazy val root = Project(id = "dicts", base = file("."))
  .enablePlugins(GitBranchPrompt)
  .settings(
    libraryDependencies ++= Dependencies.root.value,
    libraryDependencies ++= Dependencies.tests.value,
    testFrameworks += new TestFramework("weaver.framework.CatsEffect")
  )

lazy val benchmark = Project(id = "dicts-benchmark", base = file("benchmark"))
  .enablePlugins(JmhPlugin)
  .settings(
    libraryDependencies ++= Dependencies.benchmark.value,
  )
  .dependsOn(root)
