# 一、准备 Git 仓库 

**码云：https://gitee.com**

## 1、创建仓库

**通过网站右上角的「+」号，选择「新建仓库」，进入新建仓库页面**

![image](images/7ad5bbbe-68ba-49a7-a7db-9e06e8e887ac.png)



**输入名称，创建完成**

![image](images/ab67d9b6-91a5-4cc1-a345-cef3ab7014a1.png)



# 二、安装Git，设置git用户名和邮箱

## 1 . 用户名和邮箱的作用

    用户名和邮箱地址是本地git客户端一个变量 . 用户每次提交代码都会记录

## 2 .设置用户名

    git config --global user.name "username"

## 3.设置邮箱 (没有双引号)

    git config --global user.email useremail@qq.com 

## 4.查看用户名和密码

    git config user.name

    git config user.email

## 5.查看其他配置信息(git设置列表)

    git config --list

# 三、idea提交代码
## 1.打开项目并点击菜单栏上的【CVS】--》【Import into version control】--》【Create Git Repository】创建本地仓库

![image](images/38da3315-d678-4253-a916-0be039f54cca.png)



## 2.在打开的【Create Git Repository】对话框内选择本地仓库的位置，这里我选择项目的根目录。

![image](images/5c411e51-5285-43aa-93a8-6f4d015aa348.png)



## 3.右击项目点击【Git】--》【Add】，接着点击【Git】--》【Commit Directory】在打开的窗口中选择要上传到本地仓库的代码并添加注释后提交到本地仓库内。

![image](images/b0a97226-5ae6-44f3-95e5-293b0d35337f.png)



## 4.右击项目点击【Git】--》【Repository】--》【Remotes...】。在打开的【Git Remotes】窗口中添加码云的远程仓库。码云的远程仓库地址可以在码云仓库内找到。

![image](images/7e14faff-df66-4f6e-9106-289af4ce0cd3.png)



![image](images/199a3c43-16a1-433a-91ab-6e5b08d54425.png)



![image](images/54467093-61da-4350-8b2a-cc20cc9684f3.png)

## 5.点击【OK】后接着输入码云的账号密码。



## 6.上传代码到码云，右击项目点击【Git】--》【Repository】--》【Push...】在打开的【Push commits】内可以看到已提交到本地仓库的提交信息。点击【Push】按钮将本地仓库的代码上传到码云上，上传成功后就可以在码云上看到。

![image](images/0ff99483-9177-4774-bee8-99282b7b31e6.png)



![image](images/b80e2347-0c00-4c2d-80c4-f40a13fb8e65.png)