# RMI Implementation

## FEATURE

* Basic user login and register
* Clients can subscribe topics and publish comments.
* Self-implemented **remote method invocation** (RMI)
* **Multi-thread server** which can handle more than 5000 concurrency on my MAC (I also implement a concurrency client to simulate the high-concurrency scenario)
* **JMS and ActiveMQ support**. Using publisher-subscriber mode and durable subscriber.

## DESIGN

#### Overview Deign for the Publish-Subscribe Model

<img src="https://ws1.sinaimg.cn/mw690/74c2bf2dgy1g39xg9d4qrj20p80j0q5d.jpg" width="600px"/>

I have added three interfaces to the `UserUtility`. They are `createSubscription`, `getMessagesFromSubscribers`, `publishMessages`, which provide pub-sub service to the client via RMI invocation.

To implement `Publish-Subscribe Model`, I add one module called `messageService` on the server side.

#### Server Overview

```powershell
.
├── dao
│   ├── UserRepositoryImpl.java
│   └── UserRepository.java
├── model
│   └── User.java
├── rmi
│   ├── communication
│   │   ├── Message.java
│   │   ├── RemoteServiceInvoker.java
│   │   └── ServiceInvocationHandler.java
│   └── registry
│       ├── LocateSimpleRegistry.java
│       ├── RemoteObjectRef.java
│       ├── RORtbl.java
│       ├── SimpleRegistry.java
│       └── SimpleRegistryServer.java
├── Server.java
└── Service
    ├── messageService
    │   ├── DurableSubscriber.java
    │   ├── MessageTest.java
    │   ├── Publisher.java
    │   └── SubscriberMap.java
    ├── PasswordAuthentication.java
    ├── UserUtilityImpl.java
    └── UserUtility.java
```

#### Publisher-Subscriber Module Overview

```powershell
.
├── DurableSubscriber.java
├── MessageTest.java
├── Publisher.java
└── SubscriberMap.java
```

`Publisher` is responsible for publishing a message about a topic. `DurableSubscriber` can subscribe to a topic, and then get messages about that topic. Here, I implemented **Durable** subscriber, which means even if the subscriber closed its connection when the publisher published several messages, the subscriber can still receive those messages when it opened its connection again.

## How to Run

### Start Server

1. Open server directory using IDEA (You could open `pom.xml` as a project)
2. Run `SimpleRegistryServer`
3. Run `Server`

### Start Client

1. Open client directory using IDEA(You could open `pom.xml` as a project)
2. Run `client`
