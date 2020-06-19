

# 手动创建了buildSrc这个模块。步骤如下：

* 在项目根目录下新建一个名为buildSrc的文件夹(与项目里的app文件夹同级)。
* 在buildSrc文件夹里创建名为build.gradle.kts的文件，文件内容参考之前的描述。
* 在buildSrc文件夹里创建src/main/java文件夹，如下图所示。并在该文件夹下创建Dependencies.kt文件，文件内容参考之前的描述。
* build一遍你的项目，然后重启你的Android Studio，项目里就会多出一个名为buildSrc的module.