const http = require('http')

const hostname = '127.0.0.1'
const port = 3000

const server = http.createServer((req, res) => {
    // 发送 HTTP 头部 
    // HTTP 状态值: 200 : OK
    // 内容类型: text/plain
    res.statusCode = 200
    res.setHeader('Content-Type', 'text/plain')
    // 发送响应数据 "Hello World"
    res.end('Hello World\n')
})

server.listen(port, hostname, () => {
    // 终端打印如下信息
    console.log(`Server running at http://${hostname}:${port}/`)
})