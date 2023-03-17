## android ui查看工具
可以在手机查看ui布局

分为aar版本和xposed版本

### aar版本  
直接在项目依赖以下aar

activityUIHook-app-1.0.0.aar、activityUIHook-1.0.0.aar
还需要glide  'com.github.bumptech.glide:glide:4.14.2'

依赖完成后在application初始化 
UiHookManager.getInstance().init(this);

xposed版本 是一个xposed模块需要配合xposed使用
## 版本
app 运行的是xposed的版本  

Xposed_main是核心模块 主要功能都在该模块

noXposed_main 是aar版本  使用noxp_uihook模块进行运行测试

### 打包aar
需要打 Xposed_main 、noXposed_main 两模块的aar

运行gradle插件的publish 生成的aar会在repositories文件下


## 注意点

### 模块资源请使用 XModuleResources加载
加载模块里面的资源需要使用 XModuleResources

### 自定义View及第三方View不要写在xml里面
ui创建使用的是宿主app的classLoader,与模块的classLoader不同，所有即使包名相同强转的话依然会报错。建议动态加载。

### View使用tag来代替id
部分app使用的换肤框架可能会View创建的使用改id,导致模块findViewById找不到View的情况。所有使用findViewWithTag来代替。


