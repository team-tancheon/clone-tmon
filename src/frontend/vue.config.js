const path = require('path')

module.exports = {
    // vue의 빌드 파일이 아래 디렉토리 경로로 생성되도록 한다.
    outputDir: path.resolve(__dirname, "../main/resources/static"),

    // Front에서 요청을 받으면 localhost:9000으로 요청을 전달한다.
    devServer: {
        proxy: {
            '/': {
                target: 'http://localhost:9000',
                ws: true,
                changeOrigin: true
            },
        }
    }
}