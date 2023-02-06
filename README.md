##注意点
### 模块资源请使用 XModuleResources加载
加载模块里面的资源需要使用 XModuleResources

### 自定义View及第三方View不要写在xml里面
ui创建使用的是宿主app的classLoader,与模块的classLoader不同，所有即使包名相同强转的话依然会报错。建议动态加载。

### View使用tag来代替id
部分app使用的换肤框架可能会View创建的使用改id,导致模块findViewById找不到View的情况。所有使用findViewWithTag来代替。


