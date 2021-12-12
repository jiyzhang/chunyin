# 编译和部署流程

## 环境准备
* jdk8及以上版本
* maven包管理工具（https://maven.apache.org/）

## 配置文件
### 配置文件目录： src/main/resources
application.properties中的spring.profiles.active参数用来配置不同环境下的参数，下面是profile的解释
1. dev: 开发环境，mysql环境指向本地，便于开发测试
2. prod: 物理机或虚拟机环境，配置中主要包含了指向生产环境的mysql地址，和https相关配置
3. cloud: 云托管环境，取消了https配置

## 打包方式
在终端中进入项目所在目录，并执行一下代码
``mvn package -DskipTests``
编译后的产物在target目录下，chunyin-0.0.1.jar

## docker镜像打包方式
代码所在目录下执行一下指令
``docker build -t chunyin:v1 .``

## 启动方式
* 以jar包方式启动 ``java -jar chunyin-0.0.1.jar``
* 以容器方式启动 ``docker run -d -p 8080:8080 chunyin:v1``

## 腾讯云托管部署流程
依次点击 小程序开发工具-云开发-云托管-版本于配置-新建配置
在弹出框中上传项目所在文件夹，配置端口为8080，点击确认，等待云端自动编译并部署即可
