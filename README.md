# Dum-dum bot
Just fun with line bot sdk :)

## Apply Line@ Account
https://developers.line.me/messaging-api/getting-started

## Requirement
    1. java8
    2. [heroku CLI](https://devcenter.heroku.com/articles/heroku-cli)

## Deploy Heroku
[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/jerry80409/dum-dum-bot)

## Setting Environment
You should setting [heroku config variables](https://devcenter.heroku.com/articles/config-vars)

```
heroku config:set LINE_BOT_CHANNEL_TOKEN=line-channel-token
heroku config:set LINE_BOT_CHANNEL_SECRET=line-channel-secret
heroku config:set SIMSIMI_KEY=simsimi-key
```
## License
[This project is licensed under the terms of the Apache2.0 license.](https://github.com/line/line-bot-sdk-java/blob/master/LICENSE.txt)

## ToDo
1. SimSimi 聊天
2. 輸入捷運站, 隨機推薦 3 筆, 餐廳。
3. wear 建議
4. 星座

