# Auth
这是后台的认证服务，牵扯不多，不写Service层了

任务：  
- 检查指定用户是否以Admin/User身份登录
- 登录、登出

下辖数据库：（应该不会有别人想调用这俩吧？）  
- ip_auth
- user_auth

关联：  
- 注册用户需要调用user数据库的信息
- 被consumer的拦截器调用

Todo：  
注册新用户时与user服务沟通
