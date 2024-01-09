class WebSocketObserver {
    constructor(ws) {
        this.ws = ws;
        this.observers = {};
        this.ws.onmessage = (event) => this.onMessageReceived(event.data);
    }

    // 注册观察者
    subscribe(type, observer) {
        if (!this.observers[type]) {
            this.observers[type] = [];
        }
        this.observers[type].push(observer);
    }

    // 取消订阅
    unsubscribe(type, observer) {
        if (this.observers[type]) {
            this.observers[type] = this.observers[type].filter((subscriber) => subscriber !== observer);
        }
    }

    // 处理收到的消息
    onMessageReceived(message) {
        const messageData = JSON.parse(message); // 假设消息是JSON格式的
        const type = messageData.type; // 假设每个消息都有一个'type'字段表示消息类型
        if (this.observers[type]) {
            this.observers[type].forEach((observer) => observer(messageData));
        }
    }
}