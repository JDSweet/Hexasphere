# Hexasphere

This is a libGDX port of [Hexasphere.js](https://github.com/arscan/hexasphere.js/).

In the process of creating this, I've learned a lot about procedural mesh generation. These sources helped me make this port:

1. https://observablehq.com/@mourner/fast-icosphere-mesh
2. https://blog.coredumping.com/subdivision-of-icosahedrons/
3. https://web.archive.org/web/20180808214504/http://donhavey.com:80/blog/tutorials/tutorial-3-the-icosahedron-sphere/
4. https://golem.ph.utexas.edu/category/2017/12/the_icosahedron_and_e8.html
5. http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
6. https://www.classes.cs.uchicago.edu/archive/2003/fall/23700/docs/handout-04.pdf

Additionally, I also encountered a similar project (using JMonkeyEngine), but none of the code was "usable," and it mostly serves as a stylistic inspiration for what I'm going for once I implement the hexagonal tile map. [The project in question is hosted on Sourceforge](https://sourceforge.net/projects/hexmapsphere/).


==================================================================================


A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and a main class extending `Game` that sets the first screen.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.
- `android`: Android mobile platform. Needs Android SDK.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `android:lint`: performs Android project validation.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
