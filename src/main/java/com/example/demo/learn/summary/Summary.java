package com.example.demo.learn.summary;

import com.example.demo.learn.design.observer.ObserverDemo;
import com.example.demo.learn.design.single.SingleObject;
import com.example.demo.learn.io.IoDemo;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 *
 * @author hujiping
 * @date 2024/2/27 10:42 AM
 */
public class Summary {

    public static void main(String[] args) {
        
        // ArrayList 
        // 底层由数组构成、
        // 默认初始容量10、无参构造创建空数组，在add方法中初始化容量
        // 通过 >> 1 + oldCapacity 的方式1.5倍扩，扩容grow方法底层用Arrays.copy方法
        List<String> list = new ArrayList<>();
        list.add("11");

        // CopyOnWriteArrayList 
        // 并发包下的线程安全的List，写时复制，每一次写操作: 1、ReentrantLock加锁 2、都会复制一份原始数据，然后进行修改和替换
        List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.add("");
        
        // HashMap 
        // 线程不安全,在并发下扩容头插法会形成环导致遍历死循环、
        // 1.7由数组+链表组成、
        // 1.8由数组+链表|红黑树组成 当链表数量达到8，先检查数组长度是否超过64，是则将链表转成红黑树，否则将数组扩容、
        // 底层数组默认:16，扩容都是2的倍数，初始化指定容量也是扩容成2的倍数，负载因子:0.75（衡量数组是否需要扩增的一个标准：当容量达到最大容量*0.75时就会扩容）
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("key", "value");
        
        // ConcurrentHashMap 
        // 线程安全 
        // 1.7由多个segment组成，每个segment是一个HashEntry数组，默认16个segment，默认支持15个线程并发 
        // 1.8由Node数组+链表|红黑树组成，通过synchronized和cas
        Map<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("key", "val");
        
        // ConcurrentLinkedQueue 非阻塞队列：通过cas实现
        ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        
        // ArrayBlockingQueue 基于数组实现的有界阻塞队列
        // put、take方法通过 ReentrantLock和Condition（notEmpty和notFull）来实现阻塞式的生产消费模型
        BlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(10);
//        arrayBlockingQueue.put("123");
        // LinkedBlockingQueue 基于链表实现的有界|无界阻塞队列（构造方法指定有无界）
        // PriorityBlockingQueue 支持优先级的阻塞队列
        AbstractQueue<String> priorityQueue = new PriorityBlockingQueue<>();
        // SynchronousQueue 不存储元素的阻塞队列，
        // DelayQueue 延时队列，底层是通过PriorityQueue实现的一个无界队列
        BlockingQueue<String> delayQueue = new DelayQueue();
        
        // ConcurrentSkipListMap 线程安全的跳表：
        // 采用空间换时间的策略，通过在有序链表上方增加多层索引，加快数据的查找速度
        ConcurrentSkipListMap<Integer, String> concurrentSkipListMap = new ConcurrentSkipListMap<Integer, String>();

        // mysql的索引结构：
        // B树索引、
        // 哈希索引

        // B+树
        // B+树是一种自平衡的树形数据结构，常用于数据库和文件系统中
        // B+树叶子节点存储数据，非叶子节点只存储索引，会包含更多的键值对，减少树的高度，提高检索效率
        // B+树叶子节点之间通过指针连接形成一个有序链表，便于范围查询
        // B+树的分裂和合并操作来保持树的平衡

        // 聚簇索引 和 非聚簇索引 的区别
        // 每个表只有一个聚簇索引，数据按照聚簇索引的顺序在磁盘上进行物理存储（比如：主键索引）(索引结构和数据一起存放的索引)
        // 每个表有多个非聚簇索引，存储的是索引键的值和指向实际数据行的指针（非主键索引）
        
        // 索引下推：一种索引优化功能，对索引中包含的字段先做判断，过滤掉不符合条件的记录，减少回表次数。。如：覆盖索引
        
        // java内存模型（JMM）
        // java内存模型指的是一种控制并发安全的约束|规范
        // 抽象了线程工作内存和主内存的关系，共享变量必须存储在主内存
        
        // volatile 关键字
        // 保证变量的可见性
        // 禁止指令重排序：通过插入特定的 内存屏障 来禁止指令重排序
        
        // 如何实现乐观锁？
        // 1.通过版本号version机制实现：修改时对比version，不同则重试
        // 2.通过cas算法实现：内存中的值（主内存的值）与预期值（第一次从主内存获取的值）是否相等， 如果相等则将新值写入内存
        // 乐观锁会存在ABA的问题：1、通过加version或时间戳的方式解决；2、AtomicStampedReference.compareAndSet来解决
        // cas只针对单个共享变量有效，
        
        // synchronized 底层原理（本质都是对对象监视器monitor的获取）
        // 1、synchronized修饰同步代码块
        // synchronized底层是通过monitorentor和monitorexit指令来控制的
        // 当执行monitorenter指令时，获取对象监视器monitor的持有权，尝试获取对象锁，如果锁的计数器为0则表示锁可以被获取到，获取后将锁计数器设为1
        // 执行monitorexit指令时（只有持有对象锁的线程才可以执行），将锁的计数器设为0，表明锁已释放，其他线程可以来获取锁
        // 1、synchronized修饰方法
        // 使用ACC_SYNCHRONIZED标识，表明该方法是一个同步方法
        
        // Synchronized锁的升级：无锁状态 -> 偏向锁状态 -> 轻量级锁状态 -> 重量级锁状态
            // 偏向锁：当一个线程获取锁后，会在对象头中的标记为记录偏向线程ID，当前线程再次在此访问的时候，无需同步操作，提高性能
            // 轻量级锁：当其他线程也来访问的时候，偏向锁会升级为轻量级锁，轻量级锁使用CAS操作来尝试获取锁
            // 自旋锁：轻量级锁获取锁失败，则升级为自旋锁，自旋尝试获取锁
            // 重量级锁：自旋获取锁失败，则升级为重量级锁，当前线程进入阻塞状态
        
        // ReentrantLock
        // ReentrantLock是一个可重入且独占式的锁
        
        // Synchronized 和 ReentrantLock 的区别？
        // 两者都是可重入锁，指的是可以再次获取自己的内部锁
        // Synchronized是java关键字，可以修饰对象、方法等，ReentrantLock是并发包下的类，有一些高级功能：等待可中断、可实现公平锁、可实现选择性通知
        
        // ReentrantReadWriteLock
        // ReentrantReadWriteLock 实现了 ReadWriteLock，是一个可重入的读写锁，读读不互斥，其他都互斥
        
        // AQS AbstractQueuedSynchronizer 抽象队列同步器
        // AQS核心原理：
        // 1.访问共享资源时，如果共享资源空闲，当前线程直接执行，并将共享资源设置为锁定状态
        // 2.如果共享资源不空闲，则通过 CLH锁（一种虚拟的双向队列） 来实现线程阻塞等待以及唤醒时锁分配
        // CLH : 是一个虚拟的双向队列（虚拟的双向队列即不存在队列实例，仅存在结点之间的关联关系）
        // 暂时获取不到锁的线程将被加入到该队列中
        // 将每条请求共享资源的线程封装成一个 CLH 队列锁的一个结点（Node）来实现锁的分配
        // 在 CLH 队列锁中，一个节点表示一个线程，它保存着线程的引用（thread）、 当前节点在队列中的状态（waitStatus）、前驱节点（prev）、后继节点（next）
        
        // AQS常见的同步工具类
        // Semaphore 信号量：可以用来控制同时访问特定资源的线程数量
        // CountDownLatch : 允许多个线程阻塞在一个地方，直到所有线程全部执行完
        // CyclicBarrier 循环栅栏: 

        /**
         * @see IoDemo
         */
        // IO模型
        // BIO 同步阻塞IO
        // NIO 同步非阻塞IO
        // IO多路复用：基于事件驱动的IO模型，使用一个线程来管理多个连接，将多个channel注册到selector上，通过select|epoll机制来实现多个通道的IO操作
        
        // NIO的核心组件：buffer、channel、selector
        // buffer（缓冲区）：读操作将channel中的数据填充到buffer中，写操作将buffer中的数据写入channel中（get、put方法）
        // channel（通道）：read、write方法
        // selector（选择器）：所有的channel都注册在selector上，selector轮询注册的channel做相应的处理
            // select：通过轮询遍历文件描述符，发现已经就绪的文件描述符就通知应用程序可以进行IO操作（文件描述符有数量限制，1024）
            // poll：采用链表来存储文件描述符
            // epoll：基于事件通知的方式，当文件描述符就绪时，通过回调通知应用程序
        
        // AIO异步IO
        
        // 零拷贝：减少数据在用户空间和内核空间的复制，提高数据传输效率
        // 在文件系统重，零拷贝通过直接内存映射mmp来实现文件的读取和写入，避免了数据在内存中的多次复制
        
        // JVM
        // java运行时数据区：堆、栈、方法区（元空间）、本地方法区、程序计数器
        // 堆：新生代（edon、surrivor）、老年代、永久代
        // 栈：栈是由一个个栈帧组成，栈帧：局部变量表、操作数栈、动态链接、方法返回地址
        
        // 对象的创建过程：
        // 1.类加载检查：检查类是否被加载过、解析和初始化过，如果没有则执行相应的类加载过程
        // 2.分配内存空间：指针碰撞|空闲列表的方式分配内存空间
        // 3.初始化零值：将分配到的内存空间初始化为零值
        // 4.设置对象头：将这个对象属于那个类、类的元数据信息、对象的哈希码、对象的GC分代年龄等信息存放到对象头中
        // 5.执行init方法
        
        // 判断一个对象是否死亡：引用计数法、可达性分析算法
        // 1.引用计数法：给对象添加一个引用计数器，有引用的地方就+1，引用失效就-1，计数器为0则表示死亡（解决不了循环引用）
        // 2.可达性分析算法：当一个对象到 GC ROOTS 没有任何引用链相连，则证明次对象不可以，可以被回收
        // 2.1.哪些对象可以作为GC ROOTS？
        // 栈帧中局部变量表中引用的对象、本地方法中引用的对象、方法区中类静态属性引用的对象、方法区中常量引用的对象、被同步锁持有的对象、JNI引用的对象
        // 判断一个常量是废弃常量：没有任何对象引用则表示是废弃常量
        // 判断一个类是无用的类：1、该类的所有实例都已被回收；2、加载该类的类加载器已经被回收；3、该类对象的class对象没有被引用
        
        // 垃圾回收
        // 空间分配担保：
            // 为了确保在 Minor GC 之前老年代本身还有容纳新生代所有对象的剩余空间
            // 只要老年代的连续空间大于新生代对象总大小或者历次晋升的平均大小，就会进行 Minor GC，否则将进行 Full GC。
        // 垃圾收集算法：1、标记-清除算法；2、复制算法；3、标记-整理算法
            // 标记-清除算法
            // 复制算法
            // 标记-整理算法
        
        // 垃圾回收器：
        // Serial、ParNew、Parallel Scavenge（关注于吞吐量）、
        // CMS（关注于用户体验）：
            // 初始标记-并发标记-重新标记-并发清除
        // G1（关注吞吐量和用户体验）：
            // 初始标记-并发标记-最终标记-刷选回收
            // 采用标记-整理算法，更加注重整体的内存整理和碎片的减少
            // 将内存划分为多个区域,每次根据允许的收集时间，选择回收价值最大的区域进行回收（收集效率高），保证了在有限时间内尽可能高的收集效率
            
        
        // JVM参数
        // -Xms2G -Xmx2G 堆的起始和最大内存大小
        // -XX:NewSize=256m -XX:MaxNewSize=256m 新生代内存大小
        // -Xmn256m 新生代内存大小（和上面一行表示的意思一样）
        // -XX:NewRatio=1 新生代占堆内存一半
        // -XX:PermSize=20m -XX:MaxPermSize=20m 永久代初始和最大内存大小（1.8之前）
        // -XX:MetaspaceSize=20m -XX:MaxMetaspaceSize=20m 元空间内存大小（1.8）
            // 1、无论 -XX:MetaspaceSize 配置什么值，对于 64 位 JVM 来说，Metaspace 的初始容量都是 21807104（约 20.8m）
            // 2、Metaspace 由于使用不断扩容到-XX:MetaspaceSize参数指定的量，就会发生 FGC，且之后每次 Metaspace 扩容都会发生 Full GC
        
        
        // jmap -dump:format=b,file=C:\Users\SnailClimb\Desktop\heap.hprof 17340
        // jstack 9256 线程快照
        
        // 网络
        // 应用层：各种应用程序协议，如HTTP、FTP、SMTP、POP3等
        // 传输层：负责向两台终端设备进程之间的通信提供通用的数据传输服务（接受上层数据，将数据分割后交给网络层，TCP和UDP）
        // 网络层：转发和路由（）
        // 数据链路层
        // 物理层
        
        // 访问网页的全过程
        // 1、浏览器输入URL后，通过DNS获取域名对应的ip
        // 2、浏览器根据ip和端口，向目标服务器发起一个tcp请求
        // 3、浏览器在tcp连接上，向服务器发送一个http请求报文，请求获取网页内容
        // 4、服务器接收到http请求后，处理请求后，返回http响应报文给浏览器
        // 5、浏览器接收到http响应报文后，解析html，渲染页面

        // 应用层常见协议：
        // HTTP 是一种基于TCP连接的超文本传输协议
        // WebSocket 是一种基于 TCP 连接的全双工通信协议，即客户端和服务器可以同时发送和接收数据。
        // SMPT 是一种基于TCP协议，用于发送邮件的协议
        // POP3/IMAP 是一种用于邮件接收的协议
        // FTP 是一种基于TCP协议的用于传输文件的协议
        // Telnet:远程登陆协议
        // SSH:安全的网络传输协议
        // RTP:实时传输协议
        // DNS:域名系统，基于 UDP 协议，用于解决域名和 IP 地址的映射问题
        
        // TCP是面向有连接的，可靠的，基于字节流的协议。
        // TCP的三次握手
            // 客户端发送一个SYN的数据包给服务端，请求建立连接，客户端进入SYN_SEND状态
            // 服务端发送一个ACK和SYN给客户端，同意建立连接，服务端进入SYN_RECV状态
            // 客户端发送一个ACK给服务端，客户端和服务端进入ESTABLISHED状态
        // TCP的四次挥手
            // 客户端发送一个FIN的数据包给服务端
            // 服务端发送一个ACK的数据包给客户端
            // 服务端再次发送一个FIN的数据包给客户端
            // 客户端发送一个ACK的数据包给服务端（等到2msl后依然没有收到回复则表示服务端正常关闭，客户端也可以正常关闭了）
        // TCP的可靠性传输：
            // 基于数据块传输（报文段）
            // 使用滑动窗口来实现流量控制-控制发送速率（接受方发送的确认报文中的窗口字段可以用来控制发送方窗口大小，从而影响发送方的发送速率）
            // 使用拥塞窗口来控制网络的拥塞程度（四种算法：慢开始、 拥塞避免、快重传 、快恢复）
            // 自动重传（使用确认和超时这两个机制，在不可靠服务的基础上实现可靠的信息传输）
        
        // 二叉树
        // 完全二叉树：除最后一层外，若其余层都是满的，并且最后一层或者是满的，或者是在右边缺少连续若干节点，则这个二叉树就是 完全二叉树
        // 平衡二叉树：如果不是空树，它的左右两个子树的高度差的绝对值不超过 1，并且左右两个子树都是一棵平衡二叉树。
            // 红黑树：自平衡二叉查找树（二查查找树：左节点 < 父节点 < 右节点，按升序或降序插入则会退化成链表，时间复杂度从logn到n），红黑树就是为了解决这个为题
            // 红黑树只要保证黑色节点平衡即可（下面第4点）
                // 1、每个节点不是黑色就是红色，根节点是黑色
                // 2、叶子节点都是黑色的空节点
                // 3、如果节点是红色，子节点一定是黑色（不会有连续的红色节点）
                // 4、从根节点到叶子节点的每条路径，必须包含相同数目的黑色节点（即相同的黑色高度）
            
        
        // mysql数据库为什么不建议使用null？
        // 1、null会额外占空间 2、null会影响聚合函数的结果（会忽略null）
        // 脏读：一个事物读取到了另一个事务没有提交的数据
        // 幻读：一个事物两次读取到的范围数据不一致
        // 不可重复度：一个事物两次读取到值不一样
        
        // mysql通过MVCC和临键锁在可重复读的隔离级别下来避免幻读
            // MVCC为每条记录生成多个版本的数据，
            // 普通读的情况下，查找不晚于事务开始时间的最新的版本读取数据（快照数据）
            // 写操作的情况下，事务会为要修改的数据行创建一个新的版本，并将修改后的数据写入新版本。
            // 更新的情况下，使用临键锁来实现数据一致性
        // 记录锁：record lock 记录锁只能锁已经存在的记录，不存在的记录则只能通过间隙锁来解决
        // 间隙锁：gap lock
        // 临键锁：next key lock （由record lock和gap lock组成）主要目的就是解决幻读的问题（因为幻读就是两次读取的范围数据不一致）
        // 在可重复读的隔离级别下，默认使用的行锁是临键锁
        
        // mysql将 IP 地址转换成整形数据存储，性能更好，占用空间也更小
        
        // mysql的主从复制
        // 1.master将sql变化写到binlog中
        // 2.slave连接主库后，从库会创建一个IO线程向主库请求binlog
        // 3.master会创建一个binlog dump线程来发送binlog
        // 4.从库的io线程接受到binlog后写入到relay log中
        // 5.从库的sql线程读取relay log同步数据
        
        // mysql主从延迟的原因？
        // 从库io线程接受binlog速度跟不上主库写入binlog的速度，导致从库relay log数据滞后于主库
        // 从库sql线程执行relay log的速度跟不上从库io线程接受binlog的速度，导致从库的数据滞后于relay log的数据
        
        // 避免使用子查询，可以用join代替（子查询的结果集会被存储到临时表中，不存在索引）
        // in 的值不要超过 500 个，in 操作可以更有效的利用索引，or 大多数情况下很少能利用到索引。
        // UNION 会把两个结果集的所有数据放到临时表中后再进行去重操作
        // pt-online-schema-change 它会首先建立一个与原表结构相同的新表，并且在新表上进行表结构的修改，然后再把原表中的数据复制到新表中，并在原表中增加一些触发器。把原表中新增的数据也复制到新表中，在行所有数据复制完成之后，把新表命名成原表，并把原来的表删除掉。把原来一个 DDL 操作，分解成多个小的批次进行。
        
        // redo log、binlog、undo log、relay log
        // redo log（事务日志）：mysql实例挂了，使用redo log来恢复数据，保证数据的持久性和一致性
        // binlog：数据备份
        // undo log（回滚日志）：保证事务的原子性
        // 修改操作的处理顺序：
            // 写入undo log日志：当事务执行修改操作室，首先将修改前的数据记录到undo log中
            // 提交事务：事务提交时，写入redo log日志中，确保事务的持久性
            // 写入磁盘：最终将对应的数据页写入磁盘

        // sql执行计划explain
        // select_type 查询的类型
            // SIMPLE：简单查询，不包含 UNION 或者子查询。
            // PRIMARY：查询中如果包含子查询或其他部分，外层的 SELECT 将被标记为 PRIMARY。
            // SUBQUERY：子查询中的第一个 SELECT。
            // UNION：在 UNION 语句中，UNION 之后出现的 SELECT。
            // DERIVED：在 FROM 中出现的子查询将被标记为 DERIVED。
            // UNION RESULT：UNION 查询的结果
        // type 查询执行的类型
            // system > const > eq_ref > ref > fulltext > ref_or_null > index_merge > unique_subquery > index_subquery > range > index > ALL
        // extra MySQL解析查询的额外信息
            // Using filesort：在排序时使用了外部的索引排序，没有用到表内索引进行排序。
            // Using temporary：MySQL 需要创建临时表来存储查询的结果，常见于 ORDER BY 和 GROUP BY。
            // Using index：表明查询使用了覆盖索引，不用回表，查询效率非常高
            // Using index condition：表示查询优化器选择使用了索引条件下推这个特性。
            // Using where：表明查询使用了 WHERE 子句进行条件过滤。一般在没有使用到索引的时候会出现
            // Using join buffer (Block Nested Loop)：连表查询的方式，表示当被驱动表的没有使用索引的时候，MySQL 会先将驱动表读出来放到 join buffer 中，再遍历被驱动表与驱动表进行查询。
        
        
        // redis 为什么这么快
        // 1.redis 数据存储在内存，内存的访问速度是磁盘的上千倍
        // 2.redis 主要是单线程事件循环（避免的多线程的上下文切换）和IO多路复用
        // 3.redis 内置了各种优化后的数据结构，性能非常高
        
        // 常见的缓存读写策略
        // 1.写-更新数据库在删除缓存，读-先读缓存，缓存没有则读数据库，再放入缓存
        // 2.写-先查缓存，缓存不存在直接更新数据库，缓存存在，更新缓存，然后缓存自己更新数据库；读-和1一样
        // 3.和2一样，只是异步更新数据库
        
        // redis能做什么？
        // redis缓存
        // 分布式锁
        // 限流
        // 消息队列
        
        // redis的数据结构：
        // String
        // List：双端链表
        // Set：无序不重复、可以做交集、并集
        // zset：排序集合
        // hash：对象
        
        // redis的持久化
        // RDB快照的方式
            // save和bgsave的方式生成RDB快照文件（save的方式会阻塞主线程，bgsave是fork出一个子线程来生成RDB文件）
        // AOF追加文件的方式
            // 1.写命令追加到AOF的缓冲区
            // 2.将AOF缓冲区的数据write到系统内核缓冲区中
            // 3.fsync刷盘到磁盘的AOF文件中（刷盘方式：一写到AOF缓冲区就刷盘；每隔1s刷盘；系统默认30s刷盘）
            // 4.AOF文件重写（文件比较大的时候）
        
        // redis大key：String类型的超过1M，其他类型的包含元素超过5000，
        // 查找大key：Redis自带的--bigkeys；scan命令查找；分析RDB文件
        // 删除大key：可以使用游标cursor的方式删除
        
        // redis热key问题？
        // 读写分离，主节点处理写请求，从节点处理读请求
        // 将热门数据分散到多个分片实例上，减少单个实例的压力
        // 二级缓存：将热点数据放置一份到JVM本地内存中
        
        // redis的慢查询问题？
        // keys、range等命令
        // save rdb文件
        
        // redis的缓存击穿、缓存穿透、缓存雪崩
        // 缓存击穿：缓存的热点数据过期了，会导致大量请求打到数据库了
            // 设置热点key不过期或者较长时间
            // 缓存预热-将热点key提前存入缓存中
        // 缓存穿透：大量没有缓存的数据，直接达到数据库
            // 做好基本的参数校验
            // 缓存无效的key，过期时间设置短一点
            // 布隆过滤器
            // 接口做限流
        // 缓存雪崩：缓存在同一时间大面积的过期，直接打到数据库
            // 设置不同的过期时间
        
        // 缓存和数据库的一致性？
        // 1.先更新数据库+异步再删除缓存（推荐）
        // 2.先删除缓存，再更新数据库（延时双删）（不推荐）
        
        // redis过期数据的删除策略？
        // 惰性删除：访问的时候，如果过期则执行删除，缺点：会有大批过期数据未删除
        // 定期删除：每隔一段时间执行删除（使用主线程操作的）
        
        // redis的内存淘汰机制？
        // 1.从已设置过期时间的数据中挑选最近最少使用的数据淘汰
        // 2.从已设置过期时间的数据中挑选将要过期的数据淘汰
        // 3.从已设置过期时间的数据中任意选择数据淘汰
        // 4.当内存不足以容纳新数据时，移除最近最少使用的 key
        // 5.从数据集中任意选择数据淘汰
        // 6.内存不足以容纳新写入数据时，新写入操作会报错

        // Redis的主从复制
        // 1.全量复制：从节点初始化的时候连接主节点，全量同步数据
        // 2.增量复制：从节点与主节点保持持续的增量复制（主节点将写命令写到缓冲区，从节点定期的从缓存区拉取数据）
        
        // @Autowired 是 Spring 提供的注解，@Resource 是 JDK 提供的注解。
        // @Autowired 默认的注入方式为byType（根据类型进行匹配）; @Resource默认注入方式为 byName（根据名称进行匹配）。
        // @Autowired 支持在构造函数、方法、字段和参数上使用。@Resource 主要用于字段和方法上的注入，不支持在构造函数或参数上使用
        
        // Bean的生命周期
        // 1.容器通过Reflection创建一个Bean的实例
        // 2.set方法设置属性值
        // 3.BeanPostProcessor的预初始化：容器在调用bean的初始化方法之前调用postProcessBeforeInitialization方法
        // 4.如果实现*Aware接口，则执行调用对应的方法
        // 5.初始化方法调用
        // 6.BeanPostProcessor的后初始化：容器在调用bean的初始化方法之后调用postProcessAfterInitialization方法
        // 7.执行destroy()方法销毁
        
        // Spring AOP 属于运行时增强，而 AspectJ 是编译时增强。 Spring AOP 基于代理(Proxying)，而 AspectJ 基于字节码操作
        
        // Spring MVC 原理？
        // DispatcherServlet接受到请求后，调用HandlerMapping
        // HandlerMapping根据url匹配能处理的handler
        // DispatcherServlet调用HandlerAdapter适配器执行Handler
        // Handler处理请求后返回一个ModelAndView给DispatcherServlet
        // 视图渲染

        // Spring事务
        // 事务的传播行为：
            // TransactionDefinition.PROPAGATION_REQUIRED 如果当前存在事务，则加入当前事务，如果当前没有事务，则创建一个新的事物（默认的）
            // TransactionDefinition.PROPAGATION_REQUIRES_NEW 不管外部方法有没有事务，都会创建一个新事物
            // TransactionDefinition.PROPAGATION_NESTED 会在当前事务内创建一个嵌套事务
            // TransactionDefinition.PROPAGATION_MANDATORY 如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
        // 事务的隔离级别：default（使用数据库默认）、读未提交、读已提交、可重复读、串行化
        // 事务的回滚策略：rollbackFor
        // @Transactional(value = "tm_jy_core", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)

        // 讲讲Spring都用到了那些设计模式？
        // 工厂模式：BeanFactory 和 ApplicationContext
            // BeanFactory 延迟注入的方式，ApplicationContext一次性全部加载
            // ApplicationContext更强大，有ClassPathXmlApplicationContext、FileSystemXmlApplicationContext、XmlWebApplicationContext
        /**
         * @see SingleObject
         */
        // 单例模式：spring的bean管理，默认方式是singleton模式
        // 代理模式：Spring AOP
        // 模版模式：JdbcTemplate
        // 装饰者模式：IO流（BufferedInputStream使用内存缓冲区对InputStream装饰增强）
        /**
         * @see ObserverDemo
         */
        // 观察者模式：ApplicationEvent、ApplicationListener、ApplicationEventPublisher
        
        // Spring的自动装配？
        // Spring通过@EnableAutoConfiguration注解开启自动装配，最终加载META-INF/spring.factories中的自动配置类实现自动装配
            // @SpringBootApplication 可以看做：@Configuration、@EnableAutoConfiguration、@ComponentScan 注解的集合
            // @ComponentScan：扫描被@Component (@Service,@Controller)注解的 bean，注解默认会扫描启动类所在的包下所有的类
            // @Configuration：允许在上下文中注册额外的 bean 或导入其他配置类
            // @EnableAutoConfiguration：启用 SpringBoot 的自动配置机制

        // JWT （JSON Web Token） 是目前最流行的跨域认证解决方案，是一种基于 Token 的认证授权机制


        // 分布式ID
            // 1.采用数据库自增id
            // 2.UUID：生成速度快，存储空间大（32个字符串，128位）、无序的（当机器时间不对的情况下需解决重复的问题）
            // 3.Snowflake：符号位 + 时间戳 + （datacenter id + worker id）+ 序列号
        
        // 分布式：
            // CAP理论：Consistency（一致性）、Availability（可用性）、Partition Tolerance（分区容错性）
                // 在网络有问题的时候（P存在的情况下），要么有C要么有A，即：CP或AP
                // 在网路没问题的时候（P不存在的情况下），是可以有CA的
            // BASE理论：在P存在的情况下，保证最终一致性（是CAP理论中AP方案的延伸）
                // 最终一致性的方案：读时修复、写时修复、异步修复
        
        
        
        // 你们的分库分表是怎么设计的？分布式事务以及分布式id？join以及聚合？讲讲 JDDL/sharding-jdbc
            // 分布式ID：使用的snowflake算法生成唯一ID（1位标识位+41位时间戳+10位机器位+12位序列号）
            // 分布式事务：（2pc和3pc都有数据一致性的问题）
                // 两段式提交（缺点：提交阶段参与者长时间无响应，协调者会在超时间内阻塞，影响性能）
                    // 准备阶段：协调者向所有参与者发送事务准备请求，参与者执行事务但不提交（占用资源不释放，阻塞），然后将状态通知协调者
                    // 提交阶段：如果所有协调者都准备就绪，协调者向所有参与者发送事务提交请求；参与者提交事务并向协调者反馈执行结果
                    // 回滚：如果任一参与者在准备或提交阶段出现问题，协调者向所有参与者发送回滚请求
                // 三段式提交：（和两段式提交的区别：在一定时间内没收到参与者反馈的提交结果，则发送回滚请求）
                    // canCommit阶段：协调者向所有参与者发送canCommit请求，参与者检查后反馈
                    // preCommit阶段：协调者向所有参与者发送preCommit请求（执行事务但不提交），同时写undo log、redo log
                    // doCommit阶段：协调者向所有参与者发送commit请求,参与者执行commit，反馈结果
            
            // 使用JDDL一种在部署在客户端的，基于jdbc database实现，具有读写分离、动态数据库配置功能。用于将sql路由到指定数据库
            // 8个实例，每个实例上128张表，总共1024张表（分库分表是自己实现的，根据waybillCode进行hash计算出db和table，使用自定义的拦截器查询指定库指定表）
            // 16个实例，每个实例32个库，一个库一个表 ，总共512张表（数据库实例：16C64G2T,时间21::55的QPS：5K）（应用实例：8C8G50G）（使用jddl实现分库分表）
            // 发货数据量。5330w/天
            // 分布式事务都有那些解决方案？
                // 1.采用tcc(使用全局事务协调器管理分布式事务的提交和回滚)
                // 2.两段式提交（会在提交阶段-参与者长时间无响应导致协调者阻塞的问题）
                // 3.三段式提交（解决两段式阻塞的问题：其实是没有解决的。。。。）
            
        
        // 分布式锁？
        // 分布式锁具体的条件：互斥、高可用、可重入、高性能、非阻塞
        // 分布式锁实现方式？mysql、redis、ZooKeeper（Redis实现分布式锁性能较高，ZooKeeper实现分布式锁可靠性更高）
            // redis的setNx：
                // set和expire必须是在同一个原子操作中
                // 怎么延长超时时间的问题：使用看门狗（watch dog）：如果操作共享资源的线程还未执行完成的话，Watch Dog 会不断地延长锁的过期时间，进而保证锁不会因为超时而被释放。
                // 如果redis的加锁的实例挂了，还没同步锁给其他节点，怎么处理？
                    // 解决方案：Redlock算法-客户端向 Redis 集群中的多个独立的 Redis 实例依次请求申请加锁，如果客户端能够和半数以上的实例成功地完成加锁操作，那么我们就认为，客户端成功地获得分布式锁，否则加锁失败。
            // 基于ZooKeeper实现分布式锁：（基于 临时顺序节点 和 Watcher（事件监听器） 实现的。）
                // 1.获取锁：当前请求创建一个临时节点，并判断是否是最小子节点，如果是则加锁成功，如果不是则在前一个节点上注册一个监听器（监听器作用：当节点释放锁后通知）
                // 1.释放锁：成功获取锁的请求执行完后，会将对应节点删除，然后出发监听器

        // Netty：是基于NIO模型的高性能网络通信框架
        
        // 你们系统的服务调用，rpc框架？Dubbo、JSF、Motan、gRPC
        // Dubbo 分布式rpc框架
            // Dubbo的组成：provider、consumer、注册中心、监控中心、配置中心、路由、远程调用
            // 负载均衡策略：随机选择、按照顺序选择、最小活跃数、一致性哈希、自定义
            // Dubbo的服务治理：服务注册于发现、负载均衡、容错机制、服务降级、限流、监控、配置管理、服务路由、动态配置
            // Dubbo的远程调用的过程：
                // 服务提供者在启动时将地址、服务信息注册到注册中心，消费者从注册中心获取服务提供者的信息
                // 消费者在引用远程服务时，Dubbo会生成一个代理对象，用于代表远程服务接口
                // 根据配置选择合适的通信协议、根据负载均衡选择一个服务提供者
                // 消费者与选定的服务器进行网络通信，发起远程调用请求
                // 服务提供者收到请求后，执行逻辑，将结果返回给调用方
            // Dubbo的容错机制
                // 失败自动恢复：调用失败时，Dubbo会自动切换到其他健康的提供者，从而实现快速失败和自动恢复。
                // 超时控制：当调用超时时，Dubbo会进行相应的处理，避免长时间的阻塞。
                // 失败重试：调用失败时，Dubbo可以进行自动的重试，以提高调用的成功率
                // 并行调用：对多个服务提供者并行调用，将多个结果合并后返回，以提高系统的容错能力。
        
        // ZooKeeper：分布式协调服务，提供了高可用、高性能、稳定的分布式数据一致性解决方案（数据存储+事件监听）
            // 典型的应用场景：
                // 命名服务：可以通过 ZooKeeper 的顺序节点生成全局唯一 ID
                // 数据发布/订阅：通过 Watcher 机制 可以很方便地实现数据发布/订阅。当你将数据发布到 ZooKeeper 被监听的节点上，其他机器可通过监听 ZooKeeper 上节点的变化来实现配置的动态更新。
                // 分布式锁：通过创建唯一节点获得分布式锁，当获得锁的一方执行完相关代码或者是挂掉之后就释放锁
            // 数据节点的分类：单个节点（status + data）
                // 持久节点
                // 持久顺序节点
                // 临时节点
                // 临时顺序节点
            // 时间监听器 watcher
        
        
        // 一致性哈希算法：核心思想是将数据和节点都映射到一个哈希环上，然后根据哈希值的顺序来确定数据属于哪个节点。当服务器增加或删除时，只影响该服务器的哈希，而不会导致整个服务集群的哈希键值重新分布。
        
        // 限流
        // 限流算法
            // 固定窗口计数法：固定时间内只能访问多少次（1秒钟内只能访问10次）
            // 滑动窗口计数法：将时间分成时间片的方式
            // 漏桶算法：往桶里放请求、从桶里拿请求处理，如果桶满了就丢弃（使用队列存储请求，从队列拿请求进行处理）
            // 令牌桶算法：往桶里放令牌，请求从桶里拿令牌，如果桶里没有令牌则丢弃
        
        
        // 分布式消息队列：
        // Kafka：高吞吐、低延迟、高可用（有可能消息会重复消费）、多用于大数据实时计算和日志收集等
        // RocketMQ：阿里出品、理论上能做到消息零丢失
            // RocketMQ 通过在一个 Topic 中配置多个队列并且每个队列维护每个消费者组的消费位置,实现了主题模式/发布订阅模式
            // 采用零拷贝（mmp + sendfile）
            // 组成：nameServer、producer、consumer、broker（消息队列存储的服务器）
            // 如何保证顺序消息：
                // 发送端将消息发送到同一个消息队列中：继承 MessageQueueSelector 指定消息队列来实现
                // 消费端需注册顺序消息监听器，继承 MessageListenerOrderly 接口用于顺序消息的消费（确保了按顺序被单线程消费）
            // 如何保证消息不丢失：
                // 同步双写：将消息先写入主节点，在同步到从节点，只有当消息成功写入主从节点后才会返回成功的响应
                // 消息ACK机制：会等待消费者的ACK确认，未收到则消息重发
                // 消息复制：主从复制，主节点发生故障后，从节点接替主节点继续提供服务
                // 刷盘机制：将消息刷盘持久化到磁盘
            // 重复消费：幂等性的考虑
            // 存储：
                // commitLog：一个broker下所有的消息都往commitLog中写
                // comsumeQueue：consumer可以根据consumeQueue查找待消费的消息（相当于commitLog的索引）
        // RabbitMQ：基于Erlang开发，并发能力强，性能好，达到微妙级
            // producer将消息发送给交换机，交换机和队列绑定
        // ActiveMQ：性能比较差，不推荐使用（已被淘汰）

        // 内存溢出：
        // 内存泄露：
        
        // todo
        // 知道哪些排序，这些排序是怎么样的？
            // 
        // 你们系统的限流是怎么做的？jsf限流、http层的限流？
        // 熔断、降级、是怎么做的？
        // 你们系统的异步交互用的是什么？了解吗？MQ
        // 调度系统用的什么？了解吗？
        // redis集群和哨兵？
        // Netty？
        // 定时/延时任务怎么做的？QuartZ、ElasticJob、easyJob
        // 单点登录SSO？
        // 菜单权限？susf为角色分配菜单，为人分配角色
        // 网关？Zuul、SpringCloud Gateway、Kong、APISIX
        // 线上问题具体排查过程？
        // vue、android、C#的基本知识？
            // 使用XML定义用户界面布局，可以使用ConstraintLayout、LinearLayout、RelativeLayout等
            // android组件：
                // Activity：用户界面的单个屏幕，用于交互和展示信息。
                // Fragment：Activity的一部分，可以独立存在或嵌入在其他Activity中。
                // Service：在后台执行长时间运行操作而没有用户界面
                // BroadcastReceiver：用于接收系统或应用发出的广播消息。
                // ContentProvider：用于管理应用数据，并提供对数据的访问。
        // linux、git的相关知识？

        
        
        // 项目
        // 拣运作业APP：采用android、网关、jsf、redis、jmq、jddl的架构模型搭建的分拣实操系统
            // 
        // 拣运工作台
        // 拣运作业APP
            // 采用全新的任务驱动方式完成拣运系统的封车、验货、分拣、发货的改造，
        // 称重量方专项
        // 集约系统：采用android、网关、jsf、redis、jmq、jddl的架构模型搭建的逆向退货系统
            // 解决redis 大key
                // 通过公司提供的redis扫描工具，扫描出大key
                    // 根据key查询业务逻辑，评估后，将使用大key的逻辑下线并用查询数据库的方式替换
                    // 写工具通过游标的方式删除大key，降低成本
            // 解决千万数据量的单表，使用jddl实现分库分表，解决单表的慢查询的问题
                // 前期方案选择：1、自己实现的拆分库 2、使用现有的jddl 3、使用商城的sharding-jdbc
                // 选用jddl后，确定拆分键：使用操作场地当做拆分键
                // 数据双写，写工具验证
        
        // web应用服务器：8C8G30G、worker应用服务器：8C12G50G
        // 数据库：核心单库：16C64G1T、核心拆分库：16G64G2T
        // redis：核心redis：16分片450G
        // es：5个协调节点（8C32G20G），3个主节点（4C16G20G），20数据节点（16C64G1760G）
        
        
        // 大促备战-时间规划
            // 9.12-9.28：压测、慢SQL、核心服务、资源巡检
            // 10.1之后：扩容、限流检查、演练
            // 10.23：部门封板，需求类禁止上线
            // 19号开启大促主会场，23号晚8小高峰，31号晚8大高峰（百补、爆品、秒杀），11月10日收尾
        // 大促备战-准备工作
            // 每周告警治理：ump告警、logbook告警、mdc告警、jmq告警、网关告警
            // 每周的核心服务治理：核心接口每周检查一次，并分析
            // 压测：调用量上次大促的3-5倍压，先压读接口，在混合压
            // 数据库慢sql、es慢sql：
            // 薄弱点和优化项：
            // 检查项：告警检查、日志清理检查、np检查
            // 资源检查和治理：redis各指标检查、es各指标检查、应用服务器指标检查、mysql指标检查
            // 对外接口配置限流
            // 演练-应急预案
        // 大促前检查项：日志级别error、dap结转关停
            
        
        
        // 核心发货功能：晚上9-11点业务高峰期，总包裹发货量：5000kw
            // 快运冷链调用：30k/天
            // 老发货调用：3k/天
            // 新发货调用：4000k/天，QPS：80/s
            // 新版发货岗发货调用：5000k/天，QPS：200/s
            // 自动化设备发货调用：9000k/天，QPS：230/s
        
        
        
        
//        /**
         
         
//         面试官，你好，我先做个自我介绍：
//         
//         我叫胡稷屏，今年31岁，来自于安徽安庆，毕业于大连海洋大学。
//         现就职于京东物流分拣转运研发组，担任软件开发工程师一职位。主要负责java后端开发和少量的vue、C#、安卓开发。
//         在职期间，主要参与分拣系统的需求评审和架构设计，并担任核心模块的开发工作。
//         除此之外，还有日常值班、线上问题排查、大促备战以及新员工指导等工作。
//         其中分拣系统是部门最核心的系统，主要为分拣中心提供验货、分拣、发货等服务。
//         在架构上，分拣系统是一个以Spring为框架的分布式系统， 主要处理来自C#客户端、安卓客户端、web页面的请求。使用jsf rpc框架进行服务间的调用，
//         MQ来实现异步处理、redis、mysql、es作为数据存储。整体架构支撑了全国2000多个分拣中心日均3kw包裹的流转。
        
//         以上就是我的自我介绍。
         
//         */
        
        
        // 说说项目中你做的比较好的地方？
        // 针对拣运作业工作台项目：
            // 首先拣运作业工作台是一个由10多个微服务组成的分布式系统，通过岗位作业的模式，为分拣中心提供验分发的服务。保证了日均3kw包裹的正常流转。
            // 1、在系统架构层面，从0到1设计了一套网格工序表并搭建基础服务用于支撑拣运作业工作台基础数据、通过网格关联场地，在通过场地关联任务，完成整个系统流程的串联。
                // 作业区表 work_area
                // 作业区 工序表 work_station
                // 场地 作业区 工序表 生成网格表 work_grid
            // 2、在系统优化层面，在发货岗开发过程中，发现分拣拦截代码冗余，调用复杂，采用策略和责任链的模式进行重构，降低了接口耗时->100ms
            // 3、参与了多个核心岗位的开发工作，顺利交付上线。
        
        // 分布式锁用的场景，为什么用？
            
        
        // 缓存预热？什么场景会用到，为什么用？
        
        
        // 你们系统的限流是怎么做的？
        
        
        // 我看你项目上用到了分库分表，你们是怎么实现的？
        
        
        // 你们大促备战都做了什么？
            // 从9月中旬开始每周告警治理、核心服务治理、资源检查治理
            // 核心接口的压测
            // 每天的数据库慢sql、es慢sql治理
            // 薄弱点和优化项治理
            // 对外服务限流配置
            // 扩容
            // 演练
            // 大促前检查项：开关检查、日志级别调整、dap结转
        
        // es集群master选举
            // 当节点检测到主节点失效后，就会向其他节点发送请求申请成为主节点，当获取集群一半以上的节点支持后，则成为主节点
        // es的force merge 和 shrink操作
            // force merge：索引级别的操作，将多个分段合并为较少的分段来减少索引的存储空间，减少磁盘占用
                // es索引由多个分片组成，每个分片由多个段组成（段是lucene索引中的基本存储单元）
            // shrink：用于减少索引的主分片和副本，也是减少索引占磁盘空间大小
        
        
        // 说下rocketMQ和kafka?
            // 首先，rocketmq和kafka都是高吞吐、低延迟的分布式消息，用于系统解耦、异步处理、削峰
            // rocketMQ由：生产者、消费者、broker、nameServer（注册中心）组成
            // kafka由：生产者、消费者、broker、zookeeper、topic 组成
            // rocketmq有发布订阅、点对点的模型；kafka只有发布订阅模型
            // rocketmq在顺序消息上比kafka支持的更好
            // rocketmq的topic是由队列组成，kafka的topic是由分区组成
            // rocketmq中的一个队列只能由一个消费组的一个消费链接，kafka的一个分区可以由一个消费组的多个消费链接
            // rocketmq 一个broker下所有队列公用一个日志文件来存储消息，kafka一个broker下的一个分区使用一个日志文件存储消息
        
        // 说下你们的架构？
            // 拣运作业工作台是一个由10多个微服务组成的分布式系统。
            // 通过ui微服务、center微服务、basic微服务生成网格工序基础数据
            // 根据 worker微服务消费上游消息生成场地维度的任务数据
            // 安卓系统发起请求，通过网关调用 web微服务，做校验逻辑
            // 调用 service微服务做逻辑处理
            // 最终有个任务调度系统做数据补偿或任务调度
            // report微服务做数据报表展示
        
        // jsf的令牌桶如何实现？如何支持所有系统的？
        
        // mysql的乐观锁和悲观锁？
            // 悲观锁：认为操作的数据可能会被其他事务修改，在操作数据时对其加锁，以确保数据的独占性，select for update
            // 乐观锁：认为操作的数据不会被其他事务修改，只在提交数据时检查是否有冲突，通过版本号或时间戳来判断数据是否被修改
        // mysql可重复读怎么避免幻读？
            // 快照读，在事务开始时，数据库会为该事务创建一个事务开始时的数据的快照，事务的查询操作会基于这个快照来获取数据，这样就不会受其他事务的影响
            // 临键锁，锁住一段范围的数据，防止别的事务插入或更新
        
        // synchronized的锁升级?
            // synchronized的锁升级是通过对象头中的锁状态字段来实现的，
            // 无锁状态->偏向锁，当一个线程获取了对象的锁时，对象头中的标记为设置为当前线程id
            // 偏向锁->轻量级锁，当其他线程尝试获取锁的时候，通过cas获取锁，升级为轻量级锁
            // 轻量级锁->重量级锁，
        
        // 频繁fullgc排查思路
            // 内存泄漏导致堆内存中对象无法被释放，导致堆内存不断增长
            // 堆大小不足，
            // 对象生命周期过长
            // 大对象
            // 过多的类加载，导致元空间内存不足，触发fullgc，调整元空间大小
        
        // 如何实现redis的最近最少使用的内存淘汰策略？
            // LinkedHashMap实现，插入顺序 且 维护了双向链表，重写get方法：删除已存在的值，然后再put，这时候值就在链表队首
        
        // 线程池的关闭 shutdown、shutdownNow
        
        // 阿里规范，为什么使用自增主键？
            // 自增主键使用自增的方式生成主键，不需要为新纪录计算主键的值，加快插入速度
            // 新纪录添加到索引的末尾，不会导致频繁的索引调整和重组，提高性能
            // 因为自增，所以避免的主键冲突
            // 属于聚簇索引，在磁盘上顺序存储，减少磁盘IO次数
        
        // 序列化方式有哪些？序列化协议有哪些？
            // java序列化，实现Serializable接口
            // json序列化，常用于web开发
            // xml序列化，将对象序列化成xml，常用于配置文件
            // messagePack，一种高效的二进制序列化格式
        
        // mybatis如何执行sql的？
            // sqlSessionFactory初始化数据库链接
            // 创建sqlSession对象
            // 根据方法名找到xml文件中的sql语句
            // 将传入的参数与sql语句进行绑定，生成完成的sql语句
            // 执行sql并返回
        // mybatis是如何将dao和mapper关联的？
            // mybatis初始化的时候会解析所有的mapper.xml文件，将nameSpace中的dao和mapper进行关联
            // 当调用dao接口的方法时，使用MapperProxyFactory创建一个代理对象，代理对象中包含sqlSession
            // 调用dao的方法时，会通过sqlSession调用对应mapper的方法
            // 将入参绑定到sql中，执行后返回结果
            
        // 一个http请求包含哪些？
            // 请求行、post、get
            // 请求头：host、Content-Type、Authorization、Cookie、Connection等信息
            // 请求体、
        
        // 项目/系统有什么难点？怎么解决的？ todo
            // 架构层面，微服务如何拆分、
            // 业务数据量评估，接入拆分库，横向拆分、竖向拆分
            // 缓存预热
            // 限流，对外接口配置限流
            // 复杂业务逻辑，实现困难，需要重构，采用策略和责任链
        
        // 分库分表的数据倾斜是如何处理的？
            // 分片再次细分，减少单个分片中的数据量
            // 我们是按照场地编码进行hash路由到指定库的，针对热点场地进一步hash到多个分片上
            // 定期数据结转
        
        // 注册中心和客户端是如何通信的？长链接？如果客户端成千上万怎么办？
            // dubbo的zookeeper是通过watcher机制，事件监听的方式通知客户端的。。。。。
        
        // 怎么动态修改数据库连接池配置？
        
        // http和https的区别？
            // http：应用层的超文本传输协议，数据传输是明文的，不加密，端口号：80
            // https：在http的基础上，加入 SSL/TLS 协议进行数据加密 和 证书保证 安全认证，端口号：443
                // 加密：SSL/TLS：对传输的数据进行对称加密，对对称加密的密钥进行非对称加密
                // 证书和数字签名：第三方机构给服务器颁发证书，对证书散列生成摘要，并对摘要加密生成数字签名，附在证书下方；
        //          服务器将证书发给客户端，客户端从第三方机构获取公钥对数字签名解密，获取摘要；
        //          客户端对证书做相同的散列获取摘要，和上面的摘要做对比，一致则身份验证成功（避免中间人窃取）
        
        // 常用的加密算法有哪些？
            // 对称加密：AES、DES
            // 非对称加密：RSA（最常见的，用于数字签名和密钥交换）、DSA（专门用于数字签名的加密算法）、
            // 哈希函数：MD5
            // 密码学协议：SSL/TLS（用于网络通信加密，基于对称和非对称加密）
        
        // 应用服务器启动后，怎么知道所有的bean都已经加载了？（应用服务器启动后，会做一些初始化的动作）
            // 当所有的bean都被加载完后，ContextRefreshedEvent 事件会被触发，实现ApplicationListener<ContextRefreshedEvent> 后做处理
        
        // 知道哪些aware接口和BeanPostProcessor接口？
            // BeanNameAware：获取自己在容器中的名字
            // BeanFactoryAware：允许bean获取对BeanFactory的引用，从而可以手动控制bean的创建过程。
            // ApplicationContextAware：允许bean获取对ApplicationContext的引用，从而可以获取Spring容器的各种服务。
            // EnvironmentAware：获取Spring环境的信息，如属性配置
        
        // spring是怎么实现依赖注入的？@Autowired底层是如何注入bean的？
            // spring容器扫描所有的bean，发现@Autowired后，则通过反射去创建依赖对象
        
        // B+树索引，叶子节点和非叶子节点存储的分别是什么？
            // 叶子节点存储的是数据行或者指向数据记录的指针
            // 非叶子节点存储的是键值对，键：索引列的值，值：指向子节点的指针
        
        // 怎么保证系统的高可用？
            // 完善的监控系统：监控系统的各个组件以及整体性能
            // 水平伸缩能力：通过扩容增加系统的负载能力，以应对突发流量，通过缩容降低成本
            // 自动化故障恢复：自动扩展、自动故障转移
            // 负载均衡：分配流量；限流：控制流量
            // 熔断、降级：保证接口稳定性
            // 数据备份和恢复：数据的持久化
            // 定期演练和压测
    }
}
