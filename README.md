# Criik-Blog-Server-Rebuild
* 包含登录拦截器、参数校验、日志系统、统一异常处理
* ~2023-10-30:基本完成用户、博客（ES附加）、评论、菜单模块
* ~2023-11-02:添加OSS设置、OSS图像上传、删除等功能，完善其他模块
* ~2023-11-06:前端对接基本完成，ES查询还没写（api暂时不更新，等全部写完一起更）
* ~2023-11-08:前端对接完了，修复部分功能

# 技术栈
* 前端：Typescript + React + Redux + Antd
* 后端：Spring + SpringMVC + Mybatis-Plus + SpringBoot + MySQL

# 模块API
## User模块

### 根路径

``````
/api/user
``````

### API

1. 发送注册邮箱验证码

   ``````
   /code
   Get
   请求参数：?email=
   ``````



2. 登录

   ```
   /login
   Post
   请求参数：
   {
       userinfo:（eamil或username）
       password:
       verificationCode:
   }
   返回参数
   {
   	expireTime
   }
   ```

3. 注册

   ``````
   /register
   Post
   请求参数:
   {
   	username:
   	password:
   	nickname:
   	email:
   	code:
   }
   返回参数
   {
   	expireTime,
   }
   ``````

4. 获取用户信息（通过token）

   ```
   /info
   Get
   返回参数
   {
      	user:{
           userId,
           username,
           nickname,
           brief,
           email,
           avatar,
           role,
      	}
   }
   ```

5. 上传头像（通过token）

   ```
   /avatar
   Post
   请求参数
   {
       file, // blob文件
   }
   ```

6. 获取头像（通过token）

   ```
   /avatar
   Get
   直接返回图片流
   ```

7. 修改用户信息（通过token）

   ```
   /info
   PATCH
   请求参数
   {
   	nickname,
   	brief,
   }
   返回参数
   {
   	updatedUser:{
   		...
   	}
   }
   ```

8. 修改用户权限（管理员方法）

   ```
   /role
   PATCH
   请求参数
   {
   	userId,
   	role,
   }
   ```

9. 获取登录验证码

   ```
   /verify
   GET
   
   ```

10. 获取criiky0的个人信息

    ```
    /criiky0
    GET
    返回参数
    {
       	user:{
            userId,
            username,
            nickname,
            brief,
            email,
            avatar,
            role,
       	}
    }
    ```

11. 获取criiky0的头像

    ```
    /criiky0/avatar
    GET
    返回参数
    ArrayBuffer
    ```

12. 修改密码（通过token）

    ```
    /password
    PATCH
    请求参数
    {
    	oldPsw:
    	newPsw:
    	pswConfirm:
    }
    ```

13. 修改Email（根据token）

    ```
    /email
    PATCH
    请求参数
    {
    	newEmail,
    	code,
    }
    返回参数
    {
    	user:{
    		...
    	}
    }
    ```

14. 根据Id获取userInfo

    ```
    /{id}
    GET
    response:{
    	user:{
    		...
    	}
    }
    ```

15. 根据id获取avatar

    ```
    /avatar/{id}
    GET
    返回参数
    ArrayBuffer
    ```

16. 登出(移除session)

    ```
    /logout
    DELETE
    ```




## Menu模块

### 根路径

```
/api/menu
```

### API

1. 添加菜单

   ```
   /
   POST
   请求参数
   // sort和level都会自动计算
   // belongMenuId为null时sort=0，level=1
   {
       title,
       icon,
       color,
       belongMenuId,
   }
   返回参数
   {
       menu:{
           ...
       }
   }
   ```

2. 删除菜单（递归删除所有子菜单和blogs）

   ```
   /
   DELETE
   请求参数
   ?menu_id=
   ```

3. 获取Criiky的菜单

   ```
   /criiky0
   GET
   返回参数
   {
   	menus:[
   		{
   			...
   			subMenu:[
   				...
   			]
   		}
   	]
   }
   ```

4. 获取对应ID的菜单

   ```
   /single/{id}
   GET
   返回参数
   {
   	menu:{
   		...
   		submenu:[
   			...
   		]
   	}
   }
   ```

5. 更新对应ID的菜单

   ```
   /
   PATCH
   请求参数
   {
   	menuId,
   	title,
   	icon,
   	color,
   	belongMenuId, // 设置为0则表示修改成null，即最上层菜单
   }
   返回参数
   {
   	updatedMenu:{
   		...
   	}
   }
   ```

6. 排序

   ```
   /sort
   PATCH
   请求参数
   [Long,...]
   ```



##  Blog模块

### 根路径

```
/api/blog
```

### API

1. 添加blog

   ```
   /
   POST
   请求参数
   {
   	title,
   	content,
   	menuId,
   }
   返回参数
   {
   	blog:{
   		...
   	}
   }
   ```

2. 获取blog

   ```
   /single/{id}
   GET
   请求参数
   ?blog_id=
   返回参数
   {
   	blog:{
   		...
   	}
   }
   ```

3. 删除blog

   ```
   /
   DELETE
   请求参数
   ?blog_id=
   ```

4. 更新blog

   ```
   /
   PATCH
   请求参数
   {
   	blogId,
   	title,
   	content,
   	collected,
   	updateAt,
   	menuId
   }
   返回参数
   {
   	updatedBlog:{
   		...
   	}
   }
   ```

5. 删除指定menu下的所有blog

   ```
   /menu
   DELETE
   请求参数
   ?menu_id=
   ```

6. 博客分页（criiky0）

   ```
   /criiky0
   GET
   请求参数(带collected参数并为true表示查询收藏的blog)
   ?page=&size=&sort=&options=collected:1
   返回参数
   {
   	"totalSize": 12,
           "records": [
               {
                   "blogId": 1718868317787013121,
                   "title": "12345666",
                   "content": "12",
                   "likes": 0,
                   "views": 0,
                   "createAt": "2023-10-30T05:51:57.000+00:00",
                   "updateAt": "2023-10-30T05:51:57.000+00:00",
                   "sort": 5,
                   "version": 1,
                   "deleted": 0,
                   "userId": 1717472052398432258,
                   "menuId": 1718504282029727746
               },
           ],
           "totalPage": 3,
           "pageSize": 5,
           "currentPage": 2
   }
   ```

7. 更新博客的likes/views

   ```
   /browse
   PATCH
   请求参数（like为true表示修改like否则修改view默认为true，plus为true为增，默认为true）
   ?blog_id=&like=true&plus=true
   返回参数
   {
   	updatedBlog:{
   		...
   	}
   }
   ```

8. 获取收藏的博客

   ```
   /criiky0/collected
   GET
   返回参数
   {
   	collectedBlogs:[
   		{
   			blogId,
   			title,
   			sort
   			createAt
   		}
   	]
   }
   ```

9. 获取博客timeline

   ```
   /criiky0/timeline
   GET
   返回参数
   timeline:[
   		{
   			blogId,
   			title,
   			createAt
   		}
   	]
   ```

10. 获取指定menu下的所有BlogDTO

    ```
    /certain_menu
    GET
    ?menu_id
    返回参数
    {
    	blogs:[
    		{
    			...
    		}
    	]
    }
    ```

11. 排序

    ```
    /sort
    PATCH
    [Long,...]
    ```

12. 获取Criiky0博客数量

    ```
    /criiky0/count
    GET
    请求参数
    ?options=collected:1
    response:{
    	count:0
    }
    ```

13. 获取有评论的Blogs（根据token）

    ```
    /hascomment
    GET
    ?page=&size=
    返回
    {
        "totalSize": 1,
        "records": [
            {
                "commentCount": 2,
                "blogId": 1720754339974189058,
                "userId": 1717472052398432258,
                "title": "12345666",
                "menuId": 1720754169400233985
        	}
        ],
        "totalPage": 1,
        "pageSize": 10,
        "currentPage": 1
    }
    ```



## Comment模块

### 根路径

```
/api/comment
```

### API

1. 添加评论

   ```
   /post
   POST
   请求参数
   {
   	content,
   	username, // 可选
   	brief, // 可选
   	belongCommentId, // 可选
   	userId, // 可选
   	blogId
   }
   返回参数
   {
   	comment:{
   		...
   	}
   }
   ```

2. 删除评论

   ```
   /
   DELETE
   请求参数
   ?comment_id=
   ```

3. 更新评论浏览数据

   ```
   /browse
   PATCH
   请求参数（plus不带默认true）
   ?comment_id=...&plus=true
   返回参数
   {
   	updatedComment:{
   		...
   	}
   }
   ```

4. 获取当前博客的Comment分页

   ```
   /curblog
   GET
   请求参数
   ?blog_id=&page=&size=&sort=&options=
   返回参数
   {
   	"comments": [
               {
                   "commentId": 1721097578677723138,
                   "content": "123",
                   "likes": 0,
                   "username": null,
                   "brief": null,
                   "createAt": "2023-11-05 17:30:13",
                   "belongCommentId": null,
                   "userId": null,
                   "blogId": 1720754339974189058,
                   "subComments": [
                       {
                           "commentId": 1721097614442553346,
                           "content": "123",
                           "likes": 0,
                           "username": null,
                           "brief": null,
                           "createAt": "2023-11-05 17:30:22",
                           "belongCommentId": 1721097578677723138,
                           "userId": null,
                           "blogId": 1720754339974189058,
                           "subComments": []
                       }
                   ]
               }
           ],
           "totalSize": 1,
           "totalPage": 1,
           "pageSize": 1,
           "pageNum": 1
   }
   ```

5. 删除当前博客所有Comment

   ```
   /blog
   DELETE
   请求参数
   ?blog_id=
   ```

6. 获取单个Comment

   ```
   /single/{id}
   GET
   返回参数
   {
   	comment:{
   		...
   	}
   }
   ```

7. 获取某个blog的评论计数

   ```
   /curblog/count
   请求参数
   ?blog_id=
   返回参数
   {
   	count:0
   }
   ```

8. 获取emoji

   ```
   /emoji
   返回参数
   {
   	emojis:{
   		...
   	}
   }
   ```



## OSSConfig

### 根路径

```
/api/oss
```

### API

1. 添加Config

   ```
   /
   POST
   请求参数
   {
   	endpoint,
   	bucket,
   	accessKeyId,
   	accessKeySecret,
   	dir, // 可选
   	callbackUrl // 可选
   }
   返回参数
   {
   	config:{
   		...
   	}
   }
   ```

2. 获取Config

   ```
   /
   GET
   {
   	config:{
   		...
   	}
   }
   ```

3. 获取Policy

   ```
   /policy
   GET
   返回参数
   {
   	"accessid":
   	"policy",
   	signature,
   	dir,
   	host,
   	expire,
   	callback
   }
   ```

4. 上传回调（OSS服务器调用，同时添加照片至应用服务器）

   ```
   /callback
   POST
   ```



## Image模块

### 根路径

```
/api/image
```

### API

1. 添加照片

   ```
   /
   POST
   请求参数
   {
   	fileName,
   	photoTime, // 可选
   }
   返回参数
   {
   	image:{
   		...
   	}
   }
   ```

2. 删除照片

   ```
   /
   DELETE
   请求参数
   ?image_id=
   ```

3. 批量上传

   ```
   /many
   POST
   请求参数
   {
   	photos:[
   		{
   			...
   		}
   	]
   }
   ```

4. 批量删除

   ```
   /many
   DELETE
   请求参数
   [id1,id2,...]
   
   ```

5. 获取criiky0照片分页

   ```
   /criiky0
   GET
   请求参数
   ?page=&size=
   返回
   {
   	"totalSize": 4,
           "records": [
               {
                   "imageId": 1721091584954343426,
                   "fileName": "blog_image/BDBTaGlj-GHj4vPAmwl_h.png",
                   "endpoint": "oss-cn-chengdu.aliyuncs.com",
                   "bucket": "criik-blog-image-storage",
                   "uploadAt": "2023-11-05 17:06:24",
                   "photoTime": "2023-11-05 17:06:24",
                   "version": 1,
                   "deleted": 0,
                   "userId": 1717472052398432258
               }
           ],
           "totalPage": 1,
           "pageSize": 5,
           "pageNum": 1		
   }
   ```

6. 获取当前用户照片分页

   ```
   /
   GET
   请求参数
   ?page=&size=
   返回
   {
   	"totalSize": 4,
           "records": [
               {
                   "imageId": 1721091584954343426,
                   "fileName": "blog_image/BDBTaGlj-GHj4vPAmwl_h.png",
                   "endpoint": "oss-cn-chengdu.aliyuncs.com",
                   "bucket": "criik-blog-image-storage",
                   "uploadAt": "2023-11-05 17:06:24",
                   "photoTime": "2023-11-05 17:06:24",
                   "version": 1,
                   "deleted": 0,
                   "userId": 1717472052398432258
               }
           ],
           "totalPage": 1,
           "pageSize": 5,
           "pageNum": 1		
   }
   ```


## ES模块

### 根路径

```
/api/es
```

### API

1. 按字段查询

   ```
   /search
   ?field=
   返回
   {
       "result": {
           "test": [
               {
                   "id": 1721776501300305922,
                   "title": "Mybatis自关联及他表关联查询",
                   "content": "<resultMap",
                   "menuTitle": "test"
               },
               {
                   "id": 1721776503695253505,
                   "title": "Mybatis自关联及他表关联查询",
                   "content": "<resultMap",
                   "menuTitle": "test"
               }
           ],
           "12345": [
               {
                   "id": 1721774105031577602,
                   "title": "<resultMap",
                   "content": "Mybatis自关联及他表关联查询",
                   "menuTitle": "12345"
               },
           ]
       }
   }
   ```

   

   


   