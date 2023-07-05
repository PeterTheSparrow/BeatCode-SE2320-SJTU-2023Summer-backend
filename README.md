# judge服务使用说明
以下为judge服务目前阶段工作原理，需要的参数以及实现的功能说明

#### 1. 使用前提
在*submission_mongo*数据库中对本次需要提交的记录有完整的信息。
*submission_mongo*中需求的字段包括：
 - submission_id
 - submission_code
 - problem_id
 - case_n
 - conf
 
 另外，也需要评测机本地的题目数据缓存目录中（路径位于"/usr/tmp/BeatCode/data"），有对应于*problem_id*的题目数据文件夹存在（评测功能不会自动更新评测机本地题目数据缓存）。
 
 *注：若环境本身未曾在/usr目录下新建/tmp目录，可能会出现权限不够的情况，需要在建立目录后执行指令：*
 >sudo chmod a=rwx /usr/tmp
 #### 2.使用过程
 在确保使用前提的情况下，只需向本服务运行的端口上发送
 > /judge?sid=(0-9+)
 
其中sid即*submission_id*。

发送后，服务端会执行对应编号的评测服务，一直等待评测完成，并最终将结果填入*submission_mongo*表中后，返回一个包含*result.txt*文件中所有内容的*String*。
#### 3.处理结果
若一切正常运行，则本服务将产生并储存以下内容：
 - result_score
 - result_time
 - result_memory
 - full_result
 - details
 	- num
    - score
    - info
    - time
    - memory
    - in
    - out
    - res

例如
![image](/api/codehub/v1/projects/7457897/uploads/a7148952-0190-448a-89df-6b812b4c12a1/1688541766766.png '1688541766766.png')

#### 4.其他说明和todo
1.题目数据文件更新逻辑
2.评测错误状态处理