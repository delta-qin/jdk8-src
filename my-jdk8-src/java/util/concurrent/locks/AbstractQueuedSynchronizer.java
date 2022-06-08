/*
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent.locks;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import sun.misc.Unsafe;

/**
 * Provides a framework for implementing blocking locks and related
 * synchronizers (semaphores, events, etc) that rely on
 * first-in-first-out (FIFO) wait queues.  This class is designed to
 * be a useful basis for most kinds of synchronizers that rely on a
 * single atomic {@code int} value to represent state. Subclasses
 * must define the protected methods that change this state, and which
 * define what that state means in terms of this object being acquired
 * or released.  Given these, the other methods in this class carry
 * out all queuing and blocking mechanics. Subclasses can maintain
 * other state fields, but only the atomically updated {@code int}
 * value manipulated using methods {@link #getState}, {@link
 * #setState} and {@link #compareAndSetState} is tracked with respect
 * to synchronization.
 * <p>
 * 为实现那些依赖于先进先出等待队列的阻塞式锁和相关同步器（信号量、事件等），提供了框架。
 * 设计这个类的目的是：为大多数依赖于单个原子int变量来表示状态的同步器 提供有用的基础。
 * 子类必须定义那些改变state的protected方法，以及这些方法定义了该state在获取或释放该对象方面的含义。
 * 鉴于此，该类中的其余方法用来执行队列和阻塞机制。
 * 子类可以维持其他状态,但是只有使用getState、setState、compareAndSetState方法原子地更新int值（指代state），才能同步跟踪状态值。
 *
 *
 * <p>Subclasses should be defined as non-public internal helper
 * classes that are used to implement the synchronization properties
 * of their enclosing class.  Class
 * {@code AbstractQueuedSynchronizer} does not implement any
 * synchronization interface.  Instead it defines methods such as
 * {@link #acquireInterruptibly} that can be invoked as
 * appropriate by concrete locks and related synchronizers to
 * implement their public methods.
 * <p>
 * 子类必须定义为非public内部辅助类，用于实现其封闭类的同步属性。
 * AQS类未实现任何同步接口。
 * 取而代之的是，它定义了诸如acquireInterruptibly的方法，具体锁和相关同步器可以根据需要调用它来实现它们的公共方法
 *
 *
 * <p>This class supports either or both a default <em>exclusive</em>
 * mode and a <em>shared</em> mode. When acquired in exclusive mode,
 * attempted acquires by other threads cannot succeed. Shared mode
 * acquires by multiple threads may (but need not) succeed. This class
 * does not &quot;understand&quot; these differences except in the
 * mechanical sense that when a shared mode acquire succeeds, the next
 * waiting thread (if one exists) must also determine whether it can
 * acquire as well. Threads waiting in the different modes share the
 * same FIFO queue. Usually, implementation subclasses support only
 * one of these modes, but both can come into play for example in a
 * {@link ReadWriteLock}. Subclasses that support only exclusive or
 * only shared modes need not define the methods supporting the unused mode.
 * <p>
 * 该类既支持默认的独占模式，又支持共享模式。
 * 在独占模式中获取时，其他线程的试图获取不会成功。
 * 在共享模式时，多个线程获取时可能会（但不需要）成功。
 * 这个类不“理解”这些差异，除非从机械意义上说，当共享模式获取成功时，下一个等待的线程（如果存在的话）必须确定得知其是否可获取成功。
 * 不同模式下的等待线程共享一个FIFO队列。
 * 通常，实现子类仅支持一种模式，但这两种模式都可以发挥作用，比如：ReadWriteLock类
 * 仅支持独占或共享模式一种的子类，不需要定义那些支持未使用模式的方法。
 *
 *
 * <p>This class defines a nested {@link ConditionObject} class that
 * can be used as a {@link Condition} implementation by subclasses
 * supporting exclusive mode for which method {@link
 * #isHeldExclusively} reports whether synchronization is exclusively
 * held with respect to the current thread, method {@link #release}
 * invoked with the current {@link #getState} value fully releases
 * this object, and {@link #acquire}, given this saved state value,
 * eventually restores this object to its previous acquired state.  No
 * {@code AbstractQueuedSynchronizer} method otherwise creates such a
 * condition, so if this constraint cannot be met, do not use it.  The
 * behavior of {@link ConditionObject} depends of course on the
 * semantics of its synchronizer implementation.
 *
 * <p>This class provides inspection, instrumentation, and monitoring
 * methods for the internal queue, as well as similar methods for
 * condition objects. These can be exported as desired into classes
 * using an {@code AbstractQueuedSynchronizer} for their
 * synchronization mechanics.
 * <p>
 * 此类为内部队列提供了检查、检测和监控的方法，也为condition对象提供了类似的方法。
 * 可以使用AbstractQueuedSynchronizer将这些同步机制按照需要导出到类中。
 *
 *
 * <p>Serialization of this class stores only the underlying atomic
 * integer maintaining state, so deserialized objects have empty
 * thread queues. Typical subclasses requiring serializability will
 * define a {@code readObject} method that restores this to a known
 * initial state upon deserialization.
 * <p>
 * 该类的序列化仅存储底层维持状态的原子整数，所以反序列化对象有空线程对象。
 * 需要序列化的子类将定义一个readObject方法，在反序列化时来恢复它到一个已知的初始化状态
 *
 *
 * <h3>Usage</h3>
 *
 * <p>To use this class as the basis of a synchronizer, redefine the
 * following methods, as applicable, by inspecting and/or modifying
 * the synchronization state using {@link #getState}, {@link
 * #setState} and/or {@link #compareAndSetState}:
 * <p>
 * 想要使用这个类作为同步器的基础，通过使用（getState、setState）方法和/或compareAndSetState方法，
 * 检查和/或修改同步状态，来按照需求重新定义以下方法
 *
 *
 * <ul>
 * <li> {@link #tryAcquire}
 * <li> {@link #tryRelease}
 * <li> {@link #tryAcquireShared}
 * <li> {@link #tryReleaseShared}
 * <li> {@link #isHeldExclusively}
 * </ul>
 * <p>
 * Each of these methods by default throws {@link
 * UnsupportedOperationException}.  Implementations of these methods
 * must be internally thread-safe, and should in general be short and
 * not block. Defining these methods is the <em>only</em> supported
 * means of using this class. All other methods are declared
 * {@code final} because they cannot be independently varied.
 * <p>
 * 默认情况下，上述的每一个方法抛出UnsupportedOperationException异常。
 * 这些方法的实现必须是内部线程安全的，并且处理同流程通常应该较短且非阻塞。
 * 定义这些方法是使用该类唯一被支持的方式。
 * 所有这些方法应该被声明为final，因为他们不能被独立更改。
 *
 *
 * <p>You may also find the inherited methods from {@link
 * AbstractOwnableSynchronizer} useful to keep track of the thread
 * owning an exclusive synchronizer.  You are encouraged to use them
 * -- this enables monitoring and diagnostic tools to assist users in
 * determining which threads hold locks.
 * <p>
 * 你可能也发现继承自AbstractOwnableSynchronizer类的方法，对于跟踪拥有独占同步器的线程非常有用。
 * 我们被鼓励使用它们--这使得监视和诊断工具能帮助用户确定哪些线程持有锁。
 *
 *
 * <p>Even though this class is based on an internal FIFO queue, it
 * does not automatically enforce FIFO acquisition policies.  The core
 * of exclusive synchronization takes the form:
 * <p>
 * 尽管这个类基于一个内部FIFO队列，但它不会自动执行FIFO获取策略。
 * 独占同步的核心遵循以下形式
 *
 *
 * <pre>
 * Acquire:
 *     while (!tryAcquire(arg)) {
 *        <em>enqueue thread if it is not already queued</em>;
 *        <em>possibly block current thread</em>;
 *        // 如果线程未排队，则入队
 * 		  // 可能阻塞当前线程
 *     }
 *
 * Release:
 *     if (tryRelease(arg))
 *        <em>unblock the first queued thread</em>;
 *        //  解除第一个排队线程的阻塞
 * </pre>
 * <p>
 * (Shared mode is similar but may involve cascading signals.)
 * 共享模式可能类似，当时可能包含级联signals
 *
 * <p id="barging">Because checks in acquire are invoked before
 * enqueuing, a newly acquiring thread may <em>barge</em> ahead of
 * others that are blocked and queued.  However, you can, if desired,
 * define {@code tryAcquire} and/or {@code tryAcquireShared} to
 * disable barging by internally invoking one or more of the inspection
 * methods, thereby providing a <em>fair</em> FIFO acquisition order.
 * In particular, most fair synchronizers can define {@code tryAcquire}
 * to return {@code false} if {@link #hasQueuedPredecessors} (a method
 * specifically designed to be used by fair synchronizers) returns
 * {@code true}.  Other variations are possible.
 * <p>
 * 因为acquire过程中的检查在排队之前被调用，一个最新acquire的线程可能会在其他被阻塞和排队的线程之前闯入（barge）。
 * 无论如何，在必要时，你可以定义tryAcquire和/或tryAcquireShared来禁用barging，通过内部调用一个或多个检查方法的方式，
 * 从而提供一个公平的FIFO获取顺序。
 * 特别地，大部分公平同步锁可以定义tryAcquire方法来返回false，如果hasQueuedPredecessors返回true。
 * hasQueuedPredecessors是一个专门为公平同步器设计的方法。
 * 其他变化是可能的。
 *
 *
 * <p>Throughput and scalability are generally highest for the
 * default barging (also known as <em>greedy</em>,
 * <em>renouncement</em>, and <em>convoy-avoidance</em>) strategy.
 * While this is not guaranteed to be fair or starvation-free, earlier
 * queued threads are allowed to recontend before later queued
 * threads, and each recontention has an unbiased chance to succeed
 * against incoming threads.  Also, while acquires do not
 * &quot;spin&quot; in the usual sense, they may perform multiple
 * invocations of {@code tryAcquire} interspersed with other
 * computations before blocking.  This gives most of the benefits of
 * spins when exclusive synchronization is only briefly held, without
 * most of the liabilities when it isn't. If so desired, you can
 * augment this by preceding calls to acquire methods with
 * "fast-path" checks, possibly prechecking {@link #hasContended}
 * and/or {@link #hasQueuedThreads} to only do so if the synchronizer
 * is likely not to be contended.
 * <p>
 * 吞吐量和可伸缩性通常是默认barging（也称为贪心、放弃和避免护送）策略的最高级别，
 * 虽然不能保证这是公平或无中断的，但允许更早排队的线程在比较晚排队线程之前重新竞争，而且每次重新竞争又一次公平地机会同传入的线程竞争成功。
 * 另外，虽然通常情况acquires不会“自旋”，但在阻塞之前，他们可能执行多次tryAcquire的调用，并附带着其他计算。
 * 当独占同步被短暂挂起时，就提供了自旋的大部分好处，没有了大部分不利因素。
 * 如果需要，你可以通过前面的调用来增强这一点，即使用“fast-path”检查来获取方法，
 * 可能会预检查hasContended()和/或hasQueuedThreads()，只有在同步器可能不存在竞争的情况下才这样做。
 *
 *
 * <p>This class provides an efficient and scalable basis for
 * synchronization in part by specializing its range of use to
 * synchronizers that can rely on {@code int} state, acquire, and
 * release parameters, and an internal FIFO wait queue. When this does
 * not suffice, you can build synchronizers from a lower level using
 * {@link java.util.concurrent.atomic atomic} classes, your own custom
 * {@link java.util.Queue} classes, and {@link LockSupport} blocking
 * support.
 * 这个类为同步提供了有效的和可伸缩的基础，通过将其使用范围专业化到（可以依赖int状态、获取和释放参数以及内部FIFO等待队列）的同步器。
 *  * 当这还未足够，你可以从使用了atomic类、自定义队列类和LockSupport阻塞支持，从较低的级别，来构建同步器
 *
 * <h3>Usage Examples</h3>
 *
 * <p>Here is a non-reentrant mutual exclusion lock class that uses
 * the value zero to represent the unlocked state, and one to
 * represent the locked state. While a non-reentrant lock
 * does not strictly require recording of the current owner
 * thread, this class does so anyway to make usage easier to monitor.
 * It also supports conditions and exposes
 * one of the instrumentation methods:
 * <p>
 * 这有一个使用0值来表示未锁状态、1值表示加锁状态的不可重入互斥独占锁类。
 * 虽然一个不可重入锁不严格要求计入当前owner的线程，但是这类还是这么做了，以便于容易见识使用状况。
 *
 *
 *  <pre> {@code
 * class Mutex implements Lock, java.io.Serializable {
 *
 *   // Our internal helper class
 *   private static class Sync extends AbstractQueuedSynchronizer {
 *     // Reports whether in locked state
 *     protected boolean isHeldExclusively() {
 *       return getState() == 1;
 *     }
 *
 *     // Acquires the lock if state is zero
 *     public boolean tryAcquire(int acquires) {
 *       assert acquires == 1; // Otherwise unused
 *       if (compareAndSetState(0, 1)) {
 *         setExclusiveOwnerThread(Thread.currentThread());
 *         return true;
 *       }
 *       return false;
 *     }
 *
 *     // Releases the lock by setting state to zero
 *     protected boolean tryRelease(int releases) {
 *       assert releases == 1; // Otherwise unused
 *       if (getState() == 0) throw new IllegalMonitorStateException();
 *       setExclusiveOwnerThread(null);
 *       setState(0);
 *       return true;
 *     }
 *
 *     // Provides a Condition
 *     Condition newCondition() { return new ConditionObject(); }
 *
 *     // Deserializes properly
 *     private void readObject(ObjectInputStream s)
 *         throws IOException, ClassNotFoundException {
 *       s.defaultReadObject();
 *       setState(0); // reset to unlocked state
 *     }
 *   }
 *
 *   // The sync object does all the hard work. We just forward to it.
 *   private final Sync sync = new Sync();
 *
 *   public void lock()                { sync.acquire(1); }
 *   public boolean tryLock()          { return sync.tryAcquire(1); }
 *   public void unlock()              { sync.release(1); }
 *   public Condition newCondition()   { return sync.newCondition(); }
 *   public boolean isLocked()         { return sync.isHeldExclusively(); }
 *   public boolean hasQueuedThreads() { return sync.hasQueuedThreads(); }
 *   public void lockInterruptibly() throws InterruptedException {
 *     sync.acquireInterruptibly(1);
 *   }
 *   public boolean tryLock(long timeout, TimeUnit unit)
 *       throws InterruptedException {
 *     return sync.tryAcquireNanos(1, unit.toNanos(timeout));
 *   }
 * }}</pre>
 *
 * <p>Here is a latch class that is like a
 * {@link java.util.concurrent.CountDownLatch CountDownLatch}
 * except that it only requires a single {@code signal} to
 * fire. Because a latch is non-exclusive, it uses the {@code shared}
 * acquire and release methods.
 * <p>
 * 这是一个类似于CountDownLatch类的latch类，只不过它仅需一个signal来触发。
 * 因为latch是非独占的，所以它使用共享模式的acquire和release方法。
 *
 *
 *  <pre> {@code
 * class BooleanLatch {
 *
 *   private static class Sync extends AbstractQueuedSynchronizer {
 *     boolean isSignalled() { return getState() != 0; }
 *
 *     protected int tryAcquireShared(int ignore) {
 *       return isSignalled() ? 1 : -1;
 *     }
 *
 *     protected boolean tryReleaseShared(int ignore) {
 *       setState(1);
 *       return true;
 *     }
 *   }
 *
 *   private final Sync sync = new Sync();
 *   public boolean isSignalled() { return sync.isSignalled(); }
 *   public void signal()         { sync.releaseShared(1); }
 *   public void await() throws InterruptedException {
 *     sync.acquireSharedInterruptibly(1);
 *   }
 * }}</pre>
 *
 * @author Doug Lea
 * @since 1.5
 */
public abstract class AbstractQueuedSynchronizer
        extends AbstractOwnableSynchronizer
        implements java.io.Serializable {
    // 各种锁子类继承用的，AQS 定义了很多如何获得锁，如何释放锁的抽象方法，目的就是为了让子类去实现；
    // AbstractOwnableSynchronizer， 作用就是为了知道当前是哪个线程获得了锁，方便监控用的

    private static final long serialVersionUID = 7373984972572414691L;

    /**
     * Creates a new {@code AbstractQueuedSynchronizer} instance
     * with initial synchronization state of zero.
     */
    protected AbstractQueuedSynchronizer() {
    }

    /**
     * Wait queue node class.
     * 等待队列Node静态内部类声明
     *
     * <p>The wait queue is a variant of a "CLH" (Craig, Landin, and
     * Hagersten) lock queue. CLH locks are normally used for
     * spinlocks.  We instead use them for blocking synchronizers, but
     * use the same basic tactic of holding some of the control
     * information about a thread in the predecessor of its node.  A
     * "status" field in each node keeps track of whether a thread
     * should block.  A node is signalled when its predecessor
     * releases.  Each node of the queue otherwise serves as a
     * specific-notification-style monitor holding a single waiting
     * thread. The status field does NOT control whether threads are
     * granted locks etc though.  A thread may try to acquire if it is
     * first in the queue. But being first does not guarantee success;
     * it only gives the right to contend.  So the currently released
     * contender thread may need to rewait.
     * <p>
     * 这个等待队列是“CLH”锁队列的变体。“CLH”锁通常用于自旋锁。
     * 相反，我们使用其阻塞同步器，但使用相同的基本策略，即在该节点的前继续节点中持有线程的一些控制信息。
     * 每个节点中的“status”字段跟踪线程是否应该阻塞。该节点的前继节点release时，该节点释放信号。
     * 否则，队列中的每个节点都充当持有单个等待线程的“特定通知样式”的监视器。
     * “status”字段不控制线程是否被授予锁。如果一个线程是队列中的第一个，它可能尝试acquire。
     * 但是成为首个并不能保证成功；它仅仅被赋予了竞争的权利。
     * 所以最近发布的竞争线程，可能需要重新等待。
     *
     *
     *
     * <p>To enqueue into a CLH lock, you atomically splice it in as new
     * tail. To dequeue, you just set the head field.
     * 要进入CLH锁队列，你需要原子地拼接新节点到队列，并作为新的tail。
     * 要退出队列，你仅仅设置head字段即可。
     *
     * <pre>
     *      +------+  prev +-----+       +-----+
     * head |      | <---- |     | <---- |     |  tail
     *      +------+       +-----+       +-----+
     * </pre>
     *
     * <p>Insertion into a CLH queue requires only a single atomic
     * operation on "tail", so there is a simple atomic point of
     * demarcation from unqueued to queued. Similarly, dequeuing
     * involves only updating the "head". However, it takes a bit
     * more work for nodes to determine who their successors are,
     * in part to deal with possible cancellation due to timeouts
     * and interrupts.
     * <p>
     * 插入CLH队列只需要在“tail”的一个原子操作，因此从无队列到队列只有一个简单的原子点划分。
     * 相似地，退出队列仅包含更新“head”节点。
     * 但是，节点需要更多的工作去决定他们的继承者是谁，一定程度上是为了解决超时和中断可能引起的取消。
     *
     *
     * <p>The "prev" links (not used in original CLH locks), are mainly
     * needed to handle cancellation. If a node is cancelled, its
     * successor is (normally) relinked to a non-cancelled
     * predecessor. For explanation of similar mechanics in the case
     * of spin locks, see the papers by Scott and Scherer at
     * http://www.cs.rochester.edu/u/scott/synchronization/
     * <p>
     * 这个"prev"指针链接（不用于原始CLH锁）主要用于处理取消。
     * 如果一个节点被取消，它的继承者（通常）会重新连接到一个未被取消的前继节点。
     * 有关自旋锁的类似机制的解释见以下地址：
     *
     *
     * <p>We also use "next" links to implement blocking mechanics.
     * The thread id for each node is kept in its own node, so a
     * predecessor signals the next node to wake up by traversing
     * next link to determine which thread it is.  Determination of
     * successor must avoid races with newly queued nodes to set
     * the "next" fields of their predecessors.  This is solved
     * when necessary by checking backwards from the atomically
     * updated "tail" when a node's successor appears to be null.
     * (Or, said differently, the next-links are an optimization
     * so that we don't usually need a backward scan.)
     * <p>
     * 我们使用“next”指针链接来实现阻塞机制。
     * 每个节点对应的线程Id都保存在自身节点中，故前继节点通过遍历next链接，来通知下一个节点苏醒，以确定他是哪个线程。
     * 确定继承节点必须避免和新排队的节点竞争，以设置前继节点的“next”指针域。
     * 当节点的继承节点可能为空时(或者换句话说，下一个链接是一个优化，因此我们通常不需要向后扫描)，
     * 从原子更新的“tail”向后检查，从而在必要时解决这个问题。
     *
     * <p>Cancellation introduces some conservatism（保守性） to the basic
     * algorithms.  Since we must poll for cancellation of other
     * nodes, we can miss noticing whether a cancelled node is
     * ahead or behind us. This is dealt with by always unparking
     * successors upon cancellation, allowing them to stabilize on
     * a new predecessor, unless we can identify an uncancelled
     * predecessor who will carry this responsibility.
     * 取消在基本算法中引入了一定的保守性。
     * 由于我们必须轮询以取消其他节点，因此我们可能遗漏注意到一个已取消的节点是在前面还是后面。
     * 解决这个问题的方法是：总是在取消后继节点前unparking，允许后继节点稳定在一个新的前继节点上。
     * 除非我们可以确定一个未被取消的，可以由其承担责任的前继节点
     *
     *
     * <p>CLH queues need a dummy header node to get started. But
     * we don't create them on construction, because it would be wasted
     * effort if there is never contention. Instead, the node
     * is constructed and head and tail pointers are set upon first
     * contention.
     * <p>
     * CLH队列需要一个虚拟的header节点来启动。
     * 但是我们不用在结构上创建，因为假如从没有竞争，这将白白浪费。
     * 反而，虚拟节点被构造，head和tail指针在第一次竞争时被设置。
     *
     * <p>Threads waiting on Conditions use the same nodes, but
     * use an additional link. Conditions only need to link nodes
     * in simple (non-concurrent) linked queues because they are
     * only accessed when exclusively held.  Upon await, a node is
     * inserted into a condition queue.  Upon signal, the node is
     * transferred to the main queue.  A special value of status
     * field is used to mark which queue a node is on.
     * 在Conditions条件等待的线程使用相同的节点，不过使用的另外一个链接。
     * 条件只需要在简单（非并发）链接队列中链接节点，因为他们只要在独占持有时才会刚被访问。
     * 等待时，将节点插入到条件队列中。
     * 接到新号时，将节点传输到主队列。
     * status字段的一个特殊值用于标记节点所在的队列
     *
     *
     * <p>Thanks go to Dave Dice, Mark Moir, Victor Luchangco, Bill
     * Scherer and Michael Scott, along with members of JSR-166
     * expert group, for helpful ideas, discussions, and critiques
     * on the design of this class.
     */
    static final class Node {
        /**
         * 同步队列单独的属性
         */
        //node 是共享模式
        static final Node SHARED = new Node();

        //node 是排它模式
        static final Node EXCLUSIVE = null;

        /**
         * Link to predecessor node that current node/thread relies on
         * for checking waitStatus. Assigned during enqueuing, and nulled
         * out (for sake of GC) only upon dequeuing.  Also, upon
         * cancellation of a predecessor, we short-circuit while
         * finding a non-cancelled one, which will always exist
         * because the head node is never cancelled: A node becomes
         * head only as a result of successful acquire. A
         * cancelled thread never succeeds in acquiring, and a thread only
         * cancels itself, not any other node.
         */
        // 当前节点的前节点，节点acquire成功之后就会变成head
        // head节点不可以被canceled
        volatile Node prev;

        /**
         * Link to the successor node that the current node/thread
         * unparks upon release. Assigned during enqueuing, adjusted
         * when bypassing cancelled predecessors, and nulled out (for
         * sake of GC) when dequeued.  The enq operation does not
         * assign next field of a predecessor until after attachment,
         * so seeing a null next field does not necessarily mean that
         * node is at end of queue. However, if a next field appears
         * to be null, we can scan prev's from the tail to
         * double-check.  The next field of cancelled nodes is set to
         * point to the node itself instead of null, to make life
         * easier for isOnSyncQueue.
         */
        volatile Node next;


        /**
         * Status field, taking on only the values: // 状态字段，仅接受以下的值
         *   SIGNAL:     The successor of this node is (or will soon be)
         *               blocked (via park), so the current node must
         *               unpark its successor when it releases or
         *               cancels. To avoid races, acquire methods must
         *               first indicate they need a signal,
         *               then retry the atomic acquire, and then,
         *               on failure, block.
         *   这个节点的后继节点是（或即将是）阻塞的（通过park），所以当它释放或取消时，当前节点必须unpark它的后继节点。
         *   为了避免竞争，acquire方法必须首先表明他们需要一个信号，然后重新尝试原子获取，然后在失败、阻塞。
         *   CANCELLED:  This node is cancelled due to timeout or interrupt.
         *               Nodes never leave this state. In particular,
         *               a thread with cancelled node never again blocks.
         *       此节点由于超时或中断而被取消。节点永远不会离开这个状态。特别是，具有取消节点的线程永远不会阻塞。
         *
         *   CONDITION:  This node is currently on a condition queue.
         *               It will not be used as a sync queue node
         *               until transferred, at which time the status
         *               will be set to 0. (Use of this value here has
         *               nothing to do with the other uses of the
         *               field, but simplifies mechanics.)
         *       此节点当前处于条件队列中。它将不会被用作同步队列节点，直到被传输到主队列，此时的状态将被设置为0。
         *       （此处使用此值与该字段的其他用途无关，只是简化了机制。）
         *
         *   PROPAGATE:  A releaseShared should be propagated to other
         *               nodes. This is set (for head node only) in
         *               doReleaseShared to ensure propagation
         *               continues, even if other operations have
         *               since intervened.
         *        已发布共享的节点应该被传播到其他节点。这是在doReleaseShared中设置（仅针对head节点）来确保传播继续，
         *        即使后来又其他操作干预
         *
         *   0:          None of the above
         *
         * The values are arranged numerically to simplify use.
         * Non-negative values mean that a node doesn't need to
         * signal. So, most code doesn't need to check for particular
         * values, just for sign.
         * 该字段使用数值以简化使用。非负值意味着节点不需要发出信号。因此，大多数代码不需要检查特定的值，只需检查签名即可。
         *
         * The field is initialized to 0 for normal sync nodes, and
         * CONDITION for condition nodes.  It is modified using CAS
         * (or when possible, unconditional volatile writes).
         * 正常同步节点，字段初始化为0；
         * 条件节点，字段初始化为CONDITION（-2）。
         * 它使用CAS进行修改(或者在可能的情况下，无条件volatile写)。
         *
         */
        /**
         * 两个队列共享的属性
         */
        // 表示当前节点的状态，通过节点的状态来控制节点的行为
        // 普通同步节点，就是 0 ，条件节点是 CONDITION -2
        volatile int waitStatus;

        // 负值表示结点处于有效等待状态，而正值表示结点已被取消。
        // 所以源码中很多地方用>0、<0来判断结点的状态是否正常。

        //* 表示当前结点已取消调度。当timeout或被中断（响应中断的情况下），
        // 会触发变更为此状态，进入该状态后的结点将不会再变化。

        static final int CANCELLED = 1;

        // 表示后继结点在等待当前结点唤醒。后继结点入队时，会将前继结点的状态更新为SIGNAL。
        //同步队列中的节点在自旋获取锁的时候，如果前一个节点的状态是 SIGNAL，
        // 那么自己就可以阻塞休息了，否则自己一直自旋尝试获得锁
        static final int SIGNAL = -1;

        //* 表示结点等待在Condition上，在条件队列中，当其他线程调用了Condition的signal()方法后，
        // CONDITION状态的结点将从 条件 队列 转移到 同步队列 中 ，等待获取同步锁。
        static final int CONDITION = -2;

        //共享模式下，前继结点不仅会唤醒其后继结点，同时也可能会唤醒后继的后继结点。
        static final int PROPAGATE = -3;


        /**
         * The thread that enqueued this node.  Initialized on
         * construction and nulled out after use.
         */
        // 当前节点的线程
        volatile Thread thread;

        /**
         * Link to next node waiting on condition, or the special
         * value SHARED.  Because condition queues are accessed only
         * when holding in exclusive mode, we just need a simple
         * linked queue to hold nodes while they are waiting on
         * conditions. They are then transferred to the queue to
         * re-acquire. And because conditions can only be exclusive,
         * we save a field by using special value to indicate shared
         * mode.
         */
        // 在同步队列中，nextWaiter 并不真的是指向其下一个节点，我们用 next 表示同步队列的下一个节点，
        //              nextWaiter 只是表示当前 Node 是排它模式还是共享模式
        // 但在条件队列中，nextWaiter 就是表示下一个节点元素
        Node nextWaiter;

        /**
         * Returns true if node is waiting in shared mode.
         */
        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        /**
         * Returns previous node, or throws NullPointerException if null.
         * Use when predecessor cannot be null.  The null check could
         * be elided, but is present to help the VM.
         *
         * @return the predecessor of this node
         */
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {    // Used to establish initial head or SHARED marker
        }

        Node(Thread thread, Node mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }

    /**
     * Head of the wait queue, lazily initialized.  Except for
     * initialization, it is modified only via method setHead.  Note:
     * If head exists, its waitStatus is guaranteed not to be
     * CANCELLED.
     */
    // 同步队列队列头
    private transient volatile Node head;

    /**
     * Tail of the wait queue, lazily initialized.  Modified only via
     * method enq to add new wait node.
     */
    // 同步队列的尾部
    private transient volatile Node tail;


    // 同步器的状态，子类会根据状态字段进行判断是否可以获得锁
    // 比如 CAS 成功给 state 赋值 1 算得到锁，赋值失败为得不到锁，
    // CAS 成功给 state 赋值 0 算释放锁，赋值失败为释放失败
    // 可重入锁，每次获得锁 +1，每次释放锁 -1
    private volatile int state;

    /**
     * Returns the current value of synchronization state.
     * This operation has memory semantics of a {@code volatile} read.
     *
     * @return current state value
     */
    protected final int getState() {
        return state;
    }

    /**
     * Sets the value of synchronization state.
     * This operation has memory semantics of a {@code volatile} write.
     *
     * @param newState the new state value
     */
    protected final void setState(int newState) {
        state = newState;
    }

    /**
     * Atomically sets synchronization state to the given updated
     * value if the current state value equals the expected value.
     * This operation has memory semantics of a {@code volatile} read
     * and write.
     *
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful. False return indicates that the actual
     * value was not equal to the expected value.
     */
    protected final boolean compareAndSetState(int expect, int update) {
        // See below for intrinsics setup to support this
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    // Queuing utilities

    /**
     * The number of nanoseconds for which it is faster to spin
     * rather than to use timed park. A rough estimate suffices
     * to improve responsiveness with very short timeouts.
     */
    // 自旋超时阀值，单位纳秒
    // 当设置等待时间时才会用到这个属性
    static final long spinForTimeoutThreshold = 1000L;

    /**
     * Inserts node into queue, initializing if necessary. See picture above.
     *
     * @param node the node to insert
     * @return node's predecessor
     */
    private Node enq(final Node node) {
        //CAS"自旋"，直到成功加入队尾
        for (; ; ) {
            Node t = tail;
            if (t == null) { // 队列为空，创建一个空的标志结点作为head结点，并将tail也指向它。
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {//正常流程，放入队尾
                node.prev = t;
                //入队存在竞争，所以CAS
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }

    /**
     * Creates and enqueues node for current thread and given mode.
     * 用于将当前线程加入到等待队列的队尾，并返回当前线程所在的结点。
     *
     * @param mode Node.EXCLUSIVE for exclusive, Node.SHARED for shared
     * @return the new node
     */
    private Node addWaiter(Node mode) {
        // 以给定模式构造结点。mode有两种：EXCLUSIVE（独占）和SHARED（共享）
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure
        // 尝试快速方式直接放到队尾。
        Node pred = tail;
        if (pred != null) {
            // 新加⼊的节点前驱节点指向尾节点
            node.prev = pred;
            // 因为如果多个线程同时获取同步状态失败都会执⾏这段代码
            // 所以，通过 CAS ⽅式确保安全的设置当前节点为最新的尾节点
            if (compareAndSetTail(pred, node)) {
                // 曾经的尾节点的后继节点指向当前节点
                pred.next = node;
                // 返回新构建的节点
                return node;
            }
        }
        // 尾节点为空，说明当前节点是第⼀个被加⼊到同步队列中的节点
        //上一步失败则通过enq入队。
        enq(node);
        return node;
    }

    /**
     * Sets head of queue to be node, thus dequeuing. Called only by
     * acquire methods.  Also nulls out unused fields for sake of GC
     * and to suppress unnecessary signals and traversals.
     *
     * @param node the node
     */
    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }

    /**
     * Wakes up node's successor, if one exists.
     * <p>
     * 用unpark()唤醒等待队列中最前边的那个未放弃线程
     *
     * @param node the node
     */
    private void unparkSuccessor(Node node) {
        //这里，node一般为当前线程所在的结点。
        int ws = node.waitStatus;
        //置零当前线程所在的结点状态，允许失败。
        if (ws < 0)
            // 清空头节点的waitStatus值，即置为0
            compareAndSetWaitStatus(node, ws, 0);

        //找到下一个需要唤醒的结点s
        Node s = node.next;
        //如果为空或已取消，是的话需要移除，重新连接队列
        if (s == null || s.waitStatus > 0) {
            s = null;
            // 从后向前找。// 从尾节点向前查找，找到队列第⼀个waitStatus状态⼩于0的节点
            for (Node t = tail; t != null && t != node; t = t.prev)
                // <=0的结点，都是还有效的结点。
                // 如果是独占式，这⾥⼩于0，其实就是 SIGNAL
                if (t.waitStatus <= 0)
                    s = t;
        }
        if (s != null)
            //唤醒  / 解除线程挂起状态
            LockSupport.unpark(s.thread);
    }

    /**
     * Release action for shared mode -- signals successor and ensures
     * propagation. (Note: For exclusive mode, release just amounts
     * to calling unparkSuccessor of head if it needs signal.)
     */
    private void doReleaseShared() {
        /*
         * Ensure that a release propagates, even if there are other
         * in-progress acquires/releases.  This proceeds in the usual
         * way of trying to unparkSuccessor of head if it needs
         * signal. But if it does not, status is set to PROPAGATE to
         * ensure that upon release, propagation continues.
         * Additionally, we must loop in case a new node is added
         * while we are doing this. Also, unlike other uses of
         * unparkSuccessor, we need to know if CAS to reset status
         * fails, if so rechecking.
         */
        for (; ; ) {
            Node h = head;
            if (h != null && h != tail) {
                int ws = h.waitStatus;
                if (ws == Node.SIGNAL) {
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                        continue;            // loop to recheck cases
                    unparkSuccessor(h); //唤醒后继
                } else if (ws == 0 &&
                        !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                    continue;                // loop on failed CAS
            }
            if (h == head)   // head发生变化                // loop if head changed
                break;
        }
    }

    /**
     * Sets head of queue, and checks if successor may be waiting
     * in shared mode, if so propagating if either propagate > 0 or
     * PROPAGATE status was set.
     *
     * @param node      the node
     * @param propagate the return value from a tryAcquireShared
     */
    private void setHeadAndPropagate(Node node, int propagate) {
        Node h = head; // Record old head for check below
        setHead(node);//head指向自己
        /*
         * Try to signal next queued node if:
         *   Propagation was indicated by caller,
         *     or was recorded (as h.waitStatus either before
         *     or after setHead) by a previous operation
         *     (note: this uses sign-check of waitStatus because
         *      PROPAGATE status may transition to SIGNAL.)
         * and
         *   The next node is waiting in shared mode,
         *     or we don't know, because it appears null
         *
         * The conservatism in both of these checks may cause
         * unnecessary wake-ups, but only when there are multiple
         * racing acquires/releases, so most need signals now or soon
         * anyway.
         */
        //如果还有剩余量，继续唤醒下一个邻居线程
        if (propagate > 0 || h == null || h.waitStatus < 0 ||
                (h = head) == null || h.waitStatus < 0) {
            Node s = node.next;
            if (s == null || s.isShared())
                doReleaseShared();
        }
    }

    // Utilities for various versions of acquire

    /**
     * Cancels an ongoing attempt to acquire.
     *
     * @param node the node
     */
//    是从等待队列中移除 CANCELLED 的 节点，并重新拼接整个队列
    private void cancelAcquire(Node node) {
        // Ignore if node doesn't exist
        if (node == null)
            return;

        // 将关联的线程信息清空
        node.thread = null;

        // 跳过同样是取消状态的前驱节点
        // Skip cancelled predecessors
        Node pred = node.prev;
        while (pred.waitStatus > 0)
            node.prev = pred = pred.prev;

        // predNext is the apparent node to unsplice. CASes below will
        // fail if not, in which case, we lost race vs another cancel
        // or signal, so no further action is necessary.
        // 跳出上⾯循环后找到前驱有效节点，并获取该有效节点的后继节点
        Node predNext = pred.next;

        // Can use unconditional write instead of CAS here.
        // After this atomic step, other Nodes can skip past us.
        // Before, we are free of interference from other threads.
        // 将当前节点的状态置为 CANCELLED
        node.waitStatus = Node.CANCELLED;

        // If we are the tail, remove ourselves.
        // 如果当前节点处在尾节点，直接从队列中删除⾃⼰就好
        if (node == tail && compareAndSetTail(node, pred)) {
            compareAndSetNext(pred, predNext, null);
        } else {
            // If successor needs signal, try to set pred's next-link
            // so it will get one. Otherwise wake it up to propagate.
            int ws;
            // 1. 如果当前节点的有效前驱节点不是头节点，也就是说当前节点不是头节点的后继节点
            if (pred != head &&
                    // 2. 判断当前节点有效前驱节点的状态是否为 SIGNAL
                    ((ws = pred.waitStatus) == Node.SIGNAL ||
                            // 3. 如果不是，尝试将前驱节点的状态置为 SIGNAL
                            (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
                    // 判断当前节点有效前驱节点的线程信息是否为空
                    pred.thread != null) {
                // 将当前节点有效前驱节点的后继节点指针指向当前节点的后继节点
                Node next = node.next;
                if (next != null && next.waitStatus <= 0)
                    compareAndSetNext(pred, predNext, next);
            } else {
                // 如果当前节点的前驱节点是头节点，或者上述其他条件不满⾜，就唤醒当前节点的后继节点
                unparkSuccessor(node);
            }

            node.next = node; // help GC
        }
    }

    /**
     * Checks and updates status for a node that failed to acquire.
     * Returns true if thread should block. This is the main signal
     * control in all acquire loops.  Requires that pred == node.prev.
     *
     * @param pred node's predecessor holding status
     * @param node the node
     * @return {@code true} if thread should block
     */
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        //拿到前驱的状态
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL)
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             */
            // 如果是 SIGNAL 状态，即等待被占⽤的资源释放，直接返回 true
            //如果已经告诉前驱拿完号后通知自己一下，那就可以安心休息了
            return true;
        // ws ⼤于0说明是CANCELLED状态，
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             */
            /*
             * 如果前驱放弃了，那就一直往前找，直到找到最近一个正常等待的状态，并排在它的后边。
             * 注意：那些放弃的结点，由于被自己“加塞”到它们前边，它们相当于形成一个无引用链，稍后就会GC回收
             */
            // 循环判断前驱节点的前驱节点是否也为CANCELLED状态，忽略该状态的节点，重新连接队列
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE.  Indicate that we
             * need a signal, but don't park yet.  Caller will need to
             * retry to make sure it cannot acquire before parking.
             */
            //如果前驱正常，那就把前驱的状态设置成SIGNAL，告诉它拿完号后通知自己一下。
            // 程序第⼀次执⾏到这返回为false，还会进⾏外层第⼆次循环，最终从返回true
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }

    /**
     * Convenience method to interrupt current thread.
     */
    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    /**
     * Convenience method 方便的方法 to park and then check if interrupted
     * 如果线程找好安全休息点后，那就可以安心去休息了。此方法就是让线程去休息，真正进入等待状态。
     *
     * @return {@code true} if interrupted
     */
    private final boolean parkAndCheckInterrupt() {
        //调用park()使线程进入waiting状态
        // 线程挂起，程序不会继续向下执⾏
        LockSupport.park(this);
        //如果被唤醒，查看自己是不是被中断的。
        // 有两种途径可以唤醒该线程：1）被unpark()；2）被interrupt()
        // Thread.interrupted()会清除当前线程的中断标记位。
//        注意在这个地方清空中断标记，在返回阻塞的acquire方法里面会检测一下有没有发生过阻塞，有的话会自己自我中断
//        也就是调用selfInterrupt();
        return Thread.interrupted();
    }

    /*
     * Various flavors of acquire, varying in exclusive/shared and
     * control modes.  Each is mostly the same, but annoyingly
     * different.  Only a little bit of factoring is possible due to
     * interactions of exception mechanics (including ensuring that we
     * cancel if tryAcquire throws exception) and other control, at
     * least not without hurting performance too much.
     */

    /**
     * Acquires in exclusive uninterruptible mode for thread already in
     * queue. Used by condition wait methods as well as acquire.
     *
     * @param node the node
     * @param arg  the acquire argument
     * @return {@code true} if interrupted while waiting
     */
//    阻塞获取锁 ，循环判断
    final boolean acquireQueued(final Node node, int arg) {
        //标记是否成功拿到资源
        boolean failed = true;
        try {
            //标记等待过程中是否被中断过
            boolean interrupted = false;

            //“自旋”
            // "死循环"，尝试获取锁，或者挂起
            for (; ; ) {
                // 拿到前驱
                final Node p = node.predecessor();
                // 如果前驱是head，即该结点已有资格去尝试获取资源。
                // （可能是老大释放完资源唤醒自己的，当然也可能被interrupt了）。
                // tryAcquire 在第一次加入的时候就尝试过只不过失败了，没拿到锁就被加入到队列里面自旋获取了

//     * 此时，再和acquireQueued()联系起来，s（等待队列中最前边的那个未放弃线程）被唤醒后，
//     * 进入if (p == head && tryAcquire(arg))的判断
//     * （即使p!=head也没关系，它会再进入shouldParkAfterFailedAcquire()寻找一个安全点。
//     * 这里既然s已经是等待队列中最前边的那个未放弃线程了，那么通过shouldParkAfterFailedAcquire()的调整，
//     * s也必然会跑到head的next结点，下一次自旋p==head就成立啦），然后s把自己设置成head标杆结点，
//     * 表示自己已经获取到资源了，acquire()也返回了！！

                // 阻塞之前再次尝试获取，（并且前一个是Head）
                if (p == head && tryAcquire(arg)) {
                    // 拿到资源后，将head指向该结点。所以head所指的标杆结点，就是当前获取到资源的那个结点或null。
                    setHead(node);
                    // setHead中node.prev已置为null，此处再将head.next置为null，就是为了方便GC回收以前的head结点。
                    // 也就意味着之前拿完资源的结点出队了
                    p.next = null;
                    failed = false; // 成功获取资源
                    //返回等待过程中是否被中断过
                    return interrupted;
                }

//                注意代码会阻塞在park parkAndCheckInterrupt里面调用的park
//                获取资源失败不会死循环：会将线程获取同步状态失败的线程挂起，
                // 如果自己可以休息了，就通过park()进入waiting状态，直到被unpark()。
                // 如果不可中断的情况下被中断了，那么会从park()中醒过来，发现拿不到资源，从而继续进入park()等待。

                // 阻塞线程,当前节点对应的线程被唤醒之后会从这里继续开始循环
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    //如果等待过程中被中断过，哪怕只有那么一次，就将interrupted标记为true
                    interrupted = true;
            }
        } finally {
            // 如果等待过程中没有成功获取资源（如timeout，或者可中断的情况下被中断了），那么取消结点在队列中的等待。
            if (failed)
//                当前节点设置为cancel状态
                cancelAcquire(node);
        }
    }

    /**
     * Acquires in exclusive interruptible mode.
     *
     * @param arg the acquire argument
     */
    private void doAcquireInterruptibly(int arg)
            throws InterruptedException {
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (; ; ) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    /**
     * Acquires in exclusive timed mode.
     *
     * @param arg          the acquire argument
     * @param nanosTimeout max wait time
     * @return {@code true} if acquired
     */
    private boolean doAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L)
            return false;
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (; ; ) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return true;
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    return false;
                if (shouldParkAfterFailedAcquire(p, node) &&
                        nanosTimeout > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    /**
     * Acquires in shared uninterruptible mode.
     *
     * @param arg the acquire argument
     */
    private void doAcquireShared(int arg) {
        //加入队列尾部
        final Node node = addWaiter(Node.SHARED);
        //是否成功标志
        boolean failed = true;
        try {
            //等待过程中是否被中断过的标志
            boolean interrupted = false;
            for (; ; ) {
                //前驱
                final Node p = node.predecessor();
                //如果到head的下一个，因为head是拿到资源的线程，
                // 此时node被唤醒，很可能是head用完资源来唤醒自己的
                if (p == head) {

                    int r = tryAcquireShared(arg);//尝试获取资源
                    if (r >= 0) {//成功
                        setHeadAndPropagate(node, r);//将head指向自己，还有剩余资源可以再唤醒之后的线程
                        p.next = null; // help GC
                        if (interrupted)//如果等待过程中被打断过，此时将中断补上。
                            selfInterrupt();
                        failed = false;
                        return;
                    }
                }

                //判断状态，寻找安全点，进入waiting状态，等着被unpark()或interrupt()
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    /**
     * Acquires in shared interruptible mode.
     *
     * @param arg the acquire argument
     */
    private void doAcquireSharedInterruptibly(int arg)
            throws InterruptedException {
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (; ; ) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    /**
     * Acquires in shared timed mode.
     *
     * @param arg          the acquire argument
     * @param nanosTimeout max wait time
     * @return {@code true} if acquired
     */
    private boolean doAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L)
            return false;
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (; ; ) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return true;
                    }
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    return false;
                if (shouldParkAfterFailedAcquire(p, node) &&
                        nanosTimeout > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    // Main exported methods

    /**
     * Attempts to acquire in exclusive mode. This method should query
     * if the state of the object permits it to be acquired in the
     * exclusive mode, and if so to acquire it.
     *
     * <p>This method is always invoked by the thread performing
     * acquire.  If this method reports failure, the acquire method
     * may queue the thread, if it is not already queued, until it is
     * signalled by a release from some other thread. This can be used
     * to implement method {@link Lock#tryLock()}.
     *
     * <p>The default
     * implementation throws {@link UnsupportedOperationException}.
     *
     * @param arg the acquire argument. This value is always the one
     *        passed to an acquire method, or is the value saved on entry
     *        to a condition wait.  The value is otherwise uninterpreted
     *        and can represent anything you like.
     * @return {@code true} if successful. Upon success, this object has
     *         been acquired.
     * @throws IllegalMonitorStateException if acquiring would place this
     *         synchronizer in an illegal state. This exception must be
     *         thrown in a consistent fashion for synchronization to work
     *         correctly.
     * @throws UnsupportedOperationException if exclusive mode is not supported
     */
    /**
     * 尝试去获取独占资源。如果获取成功，则直接返回true，否则直接返回false。
     * 这也正是tryLock()的语义，还是那句话，当然不仅仅只限于tryLock()。
     * <p>
     * AQS只是一个框架，具体资源的获取/释放方式交由自定义同步器去实现
     * <p>
     * AQS这里只定义了一个接口，具体资源的获取交由自定义同步器去实现了（通过state的get/set/CAS）
     * 至于能不能重入，能不能加塞，那就看具体的自定义同步器怎么去设计了
     * 当然，自定义同步器在进行资源访问时要考虑线程安全的影响。
     * <p>
     * 这里之所以没有定义成abstract，是因为独占模式下只用实现tryAcquire-tryRelease，
     * 而共享模式下只用实现tryAcquireShared-tryReleaseShared。
     * 如果都定义成abstract，那么每个模式也要去实现另一模式下的接口。
     * 尽量减少不必要的工作量。
     *
     * @param arg
     * @return
     */
    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to set the state to reflect a release in exclusive
     * mode.
     *
     * <p>This method is always invoked by the thread performing release.
     *
     * <p>The default implementation throws
     * {@link UnsupportedOperationException}.
     *
     * @param arg the release argument. This value is always the one
     *            passed to a release method, or the current state value upon
     *            entry to a condition wait.  The value is otherwise
     *            uninterpreted and can represent anything you like.
     * @return {@code true} if this object is now in a fully released
     * state, so that any waiting threads may attempt to acquire;
     * and {@code false} otherwise.
     * @throws IllegalMonitorStateException  if releasing would place this
     *                                       synchronizer in an illegal state. This exception must be
     *                                       thrown in a consistent fashion for synchronization to work
     *                                       correctly.
     * @throws UnsupportedOperationException if exclusive mode is not supported
     */
    // 跟tryAcquire()一样，这个方法是需要独占模式的自定义同步器去实现的。
    // 正常来说，tryRelease()都会成功的，因为这是独占模式，该线程来释放资源，
    // 那么它肯定已经拿到独占资源了，直接减掉相应量的资源即可(state-=arg)，也不需要考虑线程安全的问题。
    // 但要注意它的返回值，上面已经提到了，release()是根据tryRelease()的返回值来判断该线程是否已经完成释放掉资源了！
    // 所以自义定同步器在实现时，如果已经彻底释放资源(state=0)，要返回true，否则返回false。
    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to acquire in shared mode. This method should query if
     * the state of the object permits it to be acquired in the shared
     * mode, and if so to acquire it.
     *
     * <p>This method is always invoked by the thread performing
     * acquire.  If this method reports failure, the acquire method
     * may queue the thread, if it is not already queued, until it is
     * signalled by a release from some other thread.
     *
     * <p>The default implementation throws {@link
     * UnsupportedOperationException}.
     *
     * @param arg the acquire argument. This value is always the one
     *            passed to an acquire method, or is the value saved on entry
     *            to a condition wait.  The value is otherwise uninterpreted
     *            and can represent anything you like.
     * @return a negative value on failure; zero if acquisition in shared
     * mode succeeded but no subsequent shared-mode acquire can
     * succeed; and a positive value if acquisition in shared
     * mode succeeded and subsequent shared-mode acquires might
     * also succeed, in which case a subsequent waiting thread
     * must check availability. (Support for three different
     * return values enables this method to be used in contexts
     * where acquires only sometimes act exclusively.)  Upon
     * success, this object has been acquired.
     * @throws IllegalMonitorStateException  if acquiring would place this
     *                                       synchronizer in an illegal state. This exception must be
     *                                       thrown in a consistent fashion for synchronization to work
     *                                       correctly.
     * @throws UnsupportedOperationException if shared mode is not supported
     */
    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to set the state to reflect a release in shared mode.
     *
     * <p>This method is always invoked by the thread performing release.
     *
     * <p>The default implementation throws
     * {@link UnsupportedOperationException}.
     *
     * @param arg the release argument. This value is always the one
     *            passed to a release method, or the current state value upon
     *            entry to a condition wait.  The value is otherwise
     *            uninterpreted and can represent anything you like.
     * @return {@code true} if this release of shared mode may permit a
     * waiting acquire (shared or exclusive) to succeed; and
     * {@code false} otherwise
     * @throws IllegalMonitorStateException  if releasing would place this
     *                                       synchronizer in an illegal state. This exception must be
     *                                       thrown in a consistent fashion for synchronization to work
     *                                       correctly.
     * @throws UnsupportedOperationException if shared mode is not supported
     */
    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns {@code true} if synchronization is held exclusively with
     * respect to the current (calling) thread.  This method is invoked
     * upon each call to a non-waiting {@link ConditionObject} method.
     * (Waiting methods instead invoke {@link #release}.)
     *
     * <p>The default implementation throws {@link
     * UnsupportedOperationException}. This method is invoked
     * internally only within {@link ConditionObject} methods, so need
     * not be defined if conditions are not used.
     *
     * @return {@code true} if synchronization is held exclusively;
     * {@code false} otherwise
     * @throws UnsupportedOperationException if conditions are not supported
     */
    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    /**
     * Acquires in exclusive mode, ignoring interrupts.  Implemented
     * by invoking at least once {@link #tryAcquire},
     * returning on success.  Otherwise the thread is queued, possibly
     * repeatedly blocking and unblocking, invoking {@link
     * #tryAcquire} until success.  This method can be used
     * to implement method {@link Lock#lock}.
     * <p>
     * <p>
     * 此方法是排他模式下线程获取锁
     * 如果获取到资源，线程直接返回，否则进入等待队列，直到获取到资源为止，且整个过程忽略中断的影响。
     * 获取到资源后，线程就可以去执行其临界区代码了。
     *
     * @param arg the acquire argument.  This value is conveyed to
     *            {@link #tryAcquire} but is otherwise uninterpreted and
     *            can represent anything you like.
     */
//    排他模式下获取锁：
    public final void acquire(int arg) {
        if (
            // tryAcquire（自己实现） 先尝试直接去获取资源，如果成功则直接返回（这里体现了非公平锁，
            //              每个线程获取锁时会尝试直接抢占加塞一次，而CLH队列中可能还有别的线程在等待）
            // addWaiter()将该线程加入等待队列的尾部，并标记为排他模式；，返回当前线程所在的节点
            // acquireQueued()使线程 阻塞 在等待队列中获取资源，一直获取到资源后才返回。
            //              如果在整个等待过程中被中断过，则返回true，否则返回false。
                !tryAcquire(arg) &&
                        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            // 如果线程在等待过程中被中断过，它是不响应的。
            // 只是获取资源后才再进行自我中断selfInterrupt()，将中断补上。
            selfInterrupt();
    }

    /**
     * Acquires in exclusive mode, aborting if interrupted.
     * Implemented by first checking interrupt status, then invoking
     * at least once {@link #tryAcquire}, returning on
     * success.  Otherwise the thread is queued, possibly repeatedly
     * blocking and unblocking, invoking {@link #tryAcquire}
     * until success or the thread is interrupted.  This method can be
     * used to implement method {@link Lock#lockInterruptibly}.
     *
     * @param arg the acquire argument.  This value is conveyed to
     *            {@link #tryAcquire} but is otherwise uninterpreted and
     *            can represent anything you like.
     * @throws InterruptedException if the current thread is interrupted
     */
    public final void acquireInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (!tryAcquire(arg))
            doAcquireInterruptibly(arg);
    }

    /**
     * Attempts to acquire in exclusive mode, aborting if interrupted,
     * and failing if the given timeout elapses.  Implemented by first
     * checking interrupt status, then invoking at least once {@link
     * #tryAcquire}, returning on success.  Otherwise, the thread is
     * queued, possibly repeatedly blocking and unblocking, invoking
     * {@link #tryAcquire} until success or the thread is interrupted
     * or the timeout elapses.  This method can be used to implement
     * method {@link Lock#tryLock(long, TimeUnit)}.
     *
     * @param arg          the acquire argument.  This value is conveyed to
     *                     {@link #tryAcquire} but is otherwise uninterpreted and
     *                     can represent anything you like.
     * @param nanosTimeout the maximum number of nanoseconds to wait
     * @return {@code true} if acquired; {@code false} if timed out
     * @throws InterruptedException if the current thread is interrupted
     */
    public final boolean tryAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquire(arg) ||
                doAcquireNanos(arg, nanosTimeout);
    }

    /**
     * Releases in exclusive mode.  Implemented by unblocking one or
     * more threads if {@link #tryRelease} returns true.
     * This method can be used to implement method {@link Lock#unlock}.
     *
     * @param arg the release argument.  This value is conveyed to
     *        {@link #tryRelease} but is otherwise uninterpreted and
     *        can represent anything you like.
     * @return the value returned from {@link #tryRelease}
     */
    /**
     * 释放独占锁
     * 独占模式下线程释放共享资源的顶层入口。
     * 它会释放指定量的资源，如果彻底释放了（即state=0）,它会唤醒等待队列里的其他线程来获取资源。
     * 这也正是unlock()的语义，当然不仅仅只限于unlock()。
     *
     * @param arg
     * @return
     */
    public final boolean release(int arg) {
        // tryRelease() 释放资源
        // tryRelease()的返回值来判断该线程是否已经完成释放掉资源了！
        // 所以自定义同步器在设计tryRelease()的时候要明确这一点
        if (tryRelease(arg)) {
            //找到头结点
            // 释放成功，获取头节点
            Node h = head;
            // 存在头节点，并且waitStatus不是初始状态
            // 通过获取的过程我们已经分析了，在获取的过程中会将 waitStatus的值从初始状态更新成 SIGNAL 状态
            if (h != null && h.waitStatus != 0)
                //唤醒等待队列里的下一个线程
                unparkSuccessor(h);
            return true;
        }
        return false;
    }

    /**
     * Acquires in shared mode, ignoring interrupts.  Implemented by
     * first invoking at least once {@link #tryAcquireShared},
     * returning on success.  Otherwise the thread is queued, possibly
     * repeatedly blocking and unblocking, invoking {@link
     * #tryAcquireShared} until success.
     *
     * @param arg the acquire argument.  This value is conveyed to
     *        {@link #tryAcquireShared} but is otherwise uninterpreted
     *        and can represent anything you like.
     */
    /**
     * 共享模式下线程获取共享资源的顶层入口。
     * 它会获取指定量的资源，获取成功则直接返回，获取失败则进入等待队列，直到获取到资源为止，整个过程忽略中断。
     * <p>
     * tryAcquireShared 依然需要自定义同步器去实现。但是AQS已经把其返回值的语义定义好了：
     * 负值代表获取失败；0代表获取成功，但没有剩余资源；正数表示获取成功，还有剩余资源，其他线程还可以去获取。
     * 所以这里acquireShared()的流程就是：
     * tryAcquireShared()尝试获取资源，成功则直接返回；
     * 失败则通过doAcquireShared()进入等待队列，直到获取到资源为止才返回。
     *
     * @param arg
     */
    public final void acquireShared(int arg) {
        if (tryAcquireShared(arg) < 0)
            doAcquireShared(arg);
    }

    /**
     * Acquires in shared mode, aborting if interrupted.  Implemented
     * by first checking interrupt status, then invoking at least once
     * {@link #tryAcquireShared}, returning on success.  Otherwise the
     * thread is queued, possibly repeatedly blocking and unblocking,
     * invoking {@link #tryAcquireShared} until success or the thread
     * is interrupted.
     *
     * @param arg the acquire argument.
     *            This value is conveyed to {@link #tryAcquireShared} but is
     *            otherwise uninterpreted and can represent anything
     *            you like.
     * @throws InterruptedException if the current thread is interrupted
     */
    public final void acquireSharedInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (tryAcquireShared(arg) < 0)
            doAcquireSharedInterruptibly(arg);
    }

    /**
     * Attempts to acquire in shared mode, aborting if interrupted, and
     * failing if the given timeout elapses.  Implemented by first
     * checking interrupt status, then invoking at least once {@link
     * #tryAcquireShared}, returning on success.  Otherwise, the
     * thread is queued, possibly repeatedly blocking and unblocking,
     * invoking {@link #tryAcquireShared} until success or the thread
     * is interrupted or the timeout elapses.
     *
     * @param arg          the acquire argument.  This value is conveyed to
     *                     {@link #tryAcquireShared} but is otherwise uninterpreted
     *                     and can represent anything you like.
     * @param nanosTimeout the maximum number of nanoseconds to wait
     * @return {@code true} if acquired; {@code false} if timed out
     * @throws InterruptedException if the current thread is interrupted
     */
    public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquireShared(arg) >= 0 ||
                doAcquireSharedNanos(arg, nanosTimeout);
    }

    /**
     * Releases in shared mode.  Implemented by unblocking one or more
     * threads if {@link #tryReleaseShared} returns true.
     *
     * @param arg the release argument.  This value is conveyed to
     *        {@link #tryReleaseShared} but is otherwise uninterpreted
     *        and can represent anything you like.
     * @return the value returned from {@link #tryReleaseShared}
     */
    /**
     * 共享模式下线程释放共享资源的顶层入口
     * 释放指定量的资源，如果成功释放且允许唤醒等待线程，它会唤醒等待队列里的其他线程来获取资源。
     *
     * @param arg
     * @return
     */
    public final boolean releaseShared(int arg) {
        if (tryReleaseShared(arg)) { //尝试释放资源
            doReleaseShared(); //唤醒后继结点
            return true;
        }
        return false;
    }

    // Queue inspection methods

    /**
     * Queries whether any threads are waiting to acquire. Note that
     * because cancellations due to interrupts and timeouts may occur
     * at any time, a {@code true} return does not guarantee that any
     * other thread will ever acquire.
     *
     * <p>In this implementation, this operation returns in
     * constant time.
     *
     * @return {@code true} if there may be other threads waiting to acquire
     */
    public final boolean hasQueuedThreads() {
        return head != tail;
    }

    /**
     * Queries whether any threads have ever contended to acquire this
     * synchronizer; that is if an acquire method has ever blocked.
     *
     * <p>In this implementation, this operation returns in
     * constant time.
     *
     * @return {@code true} if there has ever been contention
     */
    public final boolean hasContended() {
        return head != null;
    }

    /**
     * Returns the first (longest-waiting) thread in the queue, or
     * {@code null} if no threads are currently queued.
     *
     * <p>In this implementation, this operation normally returns in
     * constant time, but may iterate upon contention if other threads are
     * concurrently modifying the queue.
     *
     * @return the first (longest-waiting) thread in the queue, or
     * {@code null} if no threads are currently queued
     */
    public final Thread getFirstQueuedThread() {
        // handle only fast path, else relay
        return (head == tail) ? null : fullGetFirstQueuedThread();
    }

    /**
     * Version of getFirstQueuedThread called when fastpath fails
     */
    private Thread fullGetFirstQueuedThread() {
        /*
         * The first node is normally head.next. Try to get its
         * thread field, ensuring consistent reads: If thread
         * field is nulled out or s.prev is no longer head, then
         * some other thread(s) concurrently performed setHead in
         * between some of our reads. We try this twice before
         * resorting to traversal.
         */
        Node h, s;
        Thread st;
        if (((h = head) != null && (s = h.next) != null &&
                s.prev == head && (st = s.thread) != null) ||
                ((h = head) != null && (s = h.next) != null &&
                        s.prev == head && (st = s.thread) != null))
            return st;

        /*
         * Head's next field might not have been set yet, or may have
         * been unset after setHead. So we must check to see if tail
         * is actually first node. If not, we continue on, safely
         * traversing from tail back to head to find first,
         * guaranteeing termination.
         */

        Node t = tail;
        Thread firstThread = null;
        while (t != null && t != head) {
            Thread tt = t.thread;
            if (tt != null)
                firstThread = tt;
            t = t.prev;
        }
        return firstThread;
    }

    /**
     * Returns true if the given thread is currently queued.
     *
     * <p>This implementation traverses the queue to determine
     * presence of the given thread.
     *
     * @param thread the thread
     * @return {@code true} if the given thread is on the queue
     * @throws NullPointerException if the thread is null
     */
    public final boolean isQueued(Thread thread) {
        if (thread == null)
            throw new NullPointerException();
        for (Node p = tail; p != null; p = p.prev)
            if (p.thread == thread)
                return true;
        return false;
    }

    /**
     * Returns {@code true} if the apparent first queued thread, if one
     * exists, is waiting in exclusive mode.  If this method returns
     * {@code true}, and the current thread is attempting to acquire in
     * shared mode (that is, this method is invoked from {@link
     * #tryAcquireShared}) then it is guaranteed that the current thread
     * is not the first queued thread.  Used only as a heuristic in
     * ReentrantReadWriteLock.
     */
    final boolean apparentlyFirstQueuedIsExclusive() {
        Node h, s;
        return (h = head) != null &&
                (s = h.next) != null &&
                !s.isShared() &&
                s.thread != null;
    }

    /**
     * Queries whether any threads have been waiting to acquire longer
     * than the current thread.
     *
     * <p>An invocation of this method is equivalent to (but may be
     * more efficient than):
     * <pre> {@code
     * getFirstQueuedThread() != Thread.currentThread() &&
     * hasQueuedThreads()}</pre>
     *
     * <p>Note that because cancellations due to interrupts and
     * timeouts may occur at any time, a {@code true} return does not
     * guarantee that some other thread will acquire before the current
     * thread.  Likewise, it is possible for another thread to win a
     * race to enqueue after this method has returned {@code false},
     * due to the queue being empty.
     *
     * <p>This method is designed to be used by a fair synchronizer to
     * avoid <a href="AbstractQueuedSynchronizer#barging">barging</a>.
     * Such a synchronizer's {@link #tryAcquire} method should return
     * {@code false}, and its {@link #tryAcquireShared} method should
     * return a negative value, if this method returns {@code true}
     * (unless this is a reentrant acquire).  For example, the {@code
     * tryAcquire} method for a fair, reentrant, exclusive mode
     * synchronizer might look like this:
     *
     * <pre> {@code
     * protected boolean tryAcquire(int arg) {
     *   if (isHeldExclusively()) {
     *     // A reentrant acquire; increment hold count
     *     return true;
     *   } else if (hasQueuedPredecessors()) {
     *     return false;
     *   } else {
     *     // try to acquire normally
     *   }
     * }}</pre>
     *
     * @return {@code true} if there is a queued thread preceding the
     * current thread, and {@code false} if the current thread
     * is at the head of the queue or the queue is empty
     * @since 1.7
     */
    public final boolean hasQueuedPredecessors() {
        // The correctness of this depends on head being initialized
        // before tail and on head.next being accurate if the current
        // thread is first in queue.
        Node t = tail; // Read fields in reverse initialization order
        Node h = head;
        Node s;
        return h != t &&
                ((s = h.next) == null || s.thread != Thread.currentThread());
    }


    // Instrumentation and monitoring methods

    /**
     * Returns an estimate of the number of threads waiting to
     * acquire.  The value is only an estimate because the number of
     * threads may change dynamically while this method traverses
     * internal data structures.  This method is designed for use in
     * monitoring system state, not for synchronization
     * control.
     *
     * @return the estimated number of threads waiting to acquire
     */
    public final int getQueueLength() {
        int n = 0;
        for (Node p = tail; p != null; p = p.prev) {
            if (p.thread != null)
                ++n;
        }
        return n;
    }

    /**
     * Returns a collection containing threads that may be waiting to
     * acquire.  Because the actual set of threads may change
     * dynamically while constructing this result, the returned
     * collection is only a best-effort estimate.  The elements of the
     * returned collection are in no particular order.  This method is
     * designed to facilitate construction of subclasses that provide
     * more extensive monitoring facilities.
     *
     * @return the collection of threads
     */
    public final Collection<Thread> getQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            Thread t = p.thread;
            if (t != null)
                list.add(t);
        }
        return list;
    }

    /**
     * Returns a collection containing threads that may be waiting to
     * acquire in exclusive mode. This has the same properties
     * as {@link #getQueuedThreads} except that it only returns
     * those threads waiting due to an exclusive acquire.
     *
     * @return the collection of threads
     */
    public final Collection<Thread> getExclusiveQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            if (!p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }

    /**
     * Returns a collection containing threads that may be waiting to
     * acquire in shared mode. This has the same properties
     * as {@link #getQueuedThreads} except that it only returns
     * those threads waiting due to a shared acquire.
     *
     * @return the collection of threads
     */
    public final Collection<Thread> getSharedQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            if (p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }

    /**
     * Returns a string identifying this synchronizer, as well as its state.
     * The state, in brackets, includes the String {@code "State ="}
     * followed by the current value of {@link #getState}, and either
     * {@code "nonempty"} or {@code "empty"} depending on whether the
     * queue is empty.
     *
     * @return a string identifying this synchronizer, as well as its state
     */
    public String toString() {
        int s = getState();
        String q = hasQueuedThreads() ? "non" : "";
        return super.toString() +
                "[State = " + s + ", " + q + "empty queue]";
    }


    // Internal support methods for Conditions

    /**
     * Returns true if a node, always one that was initially placed on
     * a condition queue, is now waiting to reacquire on sync queue.
     *
     * @param node the node
     * @return true if is reacquiring
     */
    final boolean isOnSyncQueue(Node node) {
        if (node.waitStatus == Node.CONDITION || node.prev == null)
            return false;
        if (node.next != null) // If has successor, it must be on queue
            return true;
        /*
         * node.prev can be non-null, but not yet on queue because
         * the CAS to place it on queue can fail. So we have to
         * traverse from tail to make sure it actually made it.  It
         * will always be near the tail in calls to this method, and
         * unless the CAS failed (which is unlikely), it will be
         * there, so we hardly ever traverse much.
         */
        return findNodeFromTail(node);
    }

    /**
     * Returns true if node is on sync queue by searching backwards from tail.
     * Called only when needed by isOnSyncQueue.
     *
     * @return true if present
     */
    private boolean findNodeFromTail(Node node) {
        Node t = tail;
        for (; ; ) {
            if (t == node)
                return true;
            if (t == null)
                return false;
            t = t.prev;
        }
    }

    /**
     * Transfers a node from a condition queue onto sync queue.
     * Returns true if successful.
     *
     * @param node the node
     * @return true if successfully transferred (else the node was
     * cancelled before signal)
     */
    final boolean transferForSignal(Node node) {
        /*
         * If cannot change waitStatus, the node has been cancelled.
         */
        if (!compareAndSetWaitStatus(node, Node.CONDITION, 0))
            return false;

        /*
         * Splice onto queue and try to set waitStatus of predecessor to
         * indicate that thread is (probably) waiting. If cancelled or
         * attempt to set waitStatus fails, wake up to resync (in which
         * case the waitStatus can be transiently and harmlessly wrong).
         */
        Node p = enq(node);
        int ws = p.waitStatus;
        if (ws > 0 || !compareAndSetWaitStatus(p, ws, Node.SIGNAL))
            LockSupport.unpark(node.thread);
        return true;
    }

    /**
     * Transfers node, if necessary, to sync queue after a cancelled wait.
     * Returns true if thread was cancelled before being signalled.
     *
     * @param node the node
     * @return true if cancelled before the node was signalled
     */
    final boolean transferAfterCancelledWait(Node node) {
        if (compareAndSetWaitStatus(node, Node.CONDITION, 0)) {
            enq(node);
            return true;
        }
        /*
         * If we lost out to a signal(), then we can't proceed
         * until it finishes its enq().  Cancelling during an
         * incomplete transfer is both rare and transient, so just
         * spin.
         */
        while (!isOnSyncQueue(node))
            Thread.yield();
        return false;
    }

    /**
     * Invokes release with current state value; returns saved state.
     * Cancels node and throws exception on failure.
     *
     * @param node the condition node for this wait
     * @return previous sync state
     */
    final int fullyRelease(Node node) {
        boolean failed = true;
        try {
            int savedState = getState();
            if (release(savedState)) {
                failed = false;
                return savedState;
            } else {
                throw new IllegalMonitorStateException();
            }
        } finally {
            if (failed)
                node.waitStatus = Node.CANCELLED;
        }
    }

    // Instrumentation methods for conditions

    /**
     * Queries whether the given ConditionObject
     * uses this synchronizer as its lock.
     *
     * @param condition the condition
     * @return {@code true} if owned
     * @throws NullPointerException if the condition is null
     */
    public final boolean owns(ConditionObject condition) {
        return condition.isOwnedBy(this);
    }

    /**
     * Queries whether any threads are waiting on the given condition
     * associated with this synchronizer. Note that because timeouts
     * and interrupts may occur at any time, a {@code true} return
     * does not guarantee that a future {@code signal} will awaken
     * any threads.  This method is designed primarily for use in
     * monitoring of the system state.
     *
     * @param condition the condition
     * @return {@code true} if there are any waiting threads
     * @throws IllegalMonitorStateException if exclusive synchronization
     *                                      is not held
     * @throws IllegalArgumentException     if the given condition is
     *                                      not associated with this synchronizer
     * @throws NullPointerException         if the condition is null
     */
    public final boolean hasWaiters(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.hasWaiters();
    }

    /**
     * Returns an estimate of the number of threads waiting on the
     * given condition associated with this synchronizer. Note that
     * because timeouts and interrupts may occur at any time, the
     * estimate serves only as an upper bound on the actual number of
     * waiters.  This method is designed for use in monitoring of the
     * system state, not for synchronization control.
     *
     * @param condition the condition
     * @return the estimated number of waiting threads
     * @throws IllegalMonitorStateException if exclusive synchronization
     *                                      is not held
     * @throws IllegalArgumentException     if the given condition is
     *                                      not associated with this synchronizer
     * @throws NullPointerException         if the condition is null
     */
    public final int getWaitQueueLength(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitQueueLength();
    }

    /**
     * Returns a collection containing those threads that may be
     * waiting on the given condition associated with this
     * synchronizer.  Because the actual set of threads may change
     * dynamically while constructing this result, the returned
     * collection is only a best-effort estimate. The elements of the
     * returned collection are in no particular order.
     *
     * @param condition the condition
     * @return the collection of threads
     * @throws IllegalMonitorStateException if exclusive synchronization
     *                                      is not held
     * @throws IllegalArgumentException     if the given condition is
     *                                      not associated with this synchronizer
     * @throws NullPointerException         if the condition is null
     */
    public final Collection<Thread> getWaitingThreads(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitingThreads();
    }

    /**
     * Condition implementation for a {@link
     * AbstractQueuedSynchronizer} serving as the basis of a {@link
     * Lock} implementation.
     *
     * <p>Method documentation for this class describes mechanics,
     * not behavioral specifications from the point of view of Lock
     * and Condition users. Exported versions of this class will in
     * general need to be accompanied by documentation describing
     * condition semantics that rely on those of the associated
     * {@code AbstractQueuedSynchronizer}.
     *
     * <p>This class is Serializable, but all fields are transient,
     * so deserialized conditions have no waiters.
     */
    // 条件队列，从属性上可以看出是链表结构
    public class ConditionObject implements Condition, java.io.Serializable {
        private static final long serialVersionUID = 1173984872572414699L;
        // 条件队列中第一个 node
        private transient Node firstWaiter;
        // 条件队列中最后一个 node
        private transient Node lastWaiter;

        public ConditionObject() {
        }

        // Internal methods

        /**
         * Adds a new waiter to wait queue.
         *
         * @return its new wait node
         */
        private Node addConditionWaiter() {
            Node t = lastWaiter;
            // If lastWaiter is cancelled, clean out.
            if (t != null && t.waitStatus != Node.CONDITION) {
                unlinkCancelledWaiters();
                t = lastWaiter;
            }
            Node node = new Node(Thread.currentThread(), Node.CONDITION);
            if (t == null)
                firstWaiter = node;
            else
                t.nextWaiter = node;
            lastWaiter = node;
            return node;
        }

        /**
         * Removes and transfers nodes until hit non-cancelled one or
         * null. Split out from signal in part to encourage compilers
         * to inline the case of no waiters.
         *
         * @param first (non-null) the first node on condition queue
         */
        private void doSignal(Node first) {
            do {
                if ((firstWaiter = first.nextWaiter) == null)
                    lastWaiter = null;
                first.nextWaiter = null;
            } while (!transferForSignal(first) &&
                    (first = firstWaiter) != null);
        }

        /**
         * Removes and transfers all nodes.
         *
         * @param first (non-null) the first node on condition queue
         */
        private void doSignalAll(Node first) {
            lastWaiter = firstWaiter = null;
            do {
                Node next = first.nextWaiter;
                first.nextWaiter = null;
                transferForSignal(first);
                first = next;
            } while (first != null);
        }

        /**
         * Unlinks cancelled waiter nodes from condition queue.
         * Called only while holding lock. This is called when
         * cancellation occurred during condition wait, and upon
         * insertion of a new waiter when lastWaiter is seen to have
         * been cancelled. This method is needed to avoid garbage
         * retention in the absence of signals. So even though it may
         * require a full traversal, it comes into play only when
         * timeouts or cancellations occur in the absence of
         * signals. It traverses all nodes rather than stopping at a
         * particular target to unlink all pointers to garbage nodes
         * without requiring many re-traversals during cancellation
         * storms.
         */
        private void unlinkCancelledWaiters() {
            Node t = firstWaiter;
            Node trail = null;
            while (t != null) {
                Node next = t.nextWaiter;
                if (t.waitStatus != Node.CONDITION) {
                    t.nextWaiter = null;
                    if (trail == null)
                        firstWaiter = next;
                    else
                        trail.nextWaiter = next;
                    if (next == null)
                        lastWaiter = trail;
                } else
                    trail = t;
                t = next;
            }
        }

        // public methods

        /**
         * Moves the longest-waiting thread, if one exists, from the
         * wait queue for this condition to the wait queue for the
         * owning lock.
         *
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *                                      returns {@code false}
         */
        public final void signal() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            Node first = firstWaiter;
            if (first != null)
                doSignal(first);
        }

        /**
         * Moves all threads from the wait queue for this condition to
         * the wait queue for the owning lock.
         *
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *                                      returns {@code false}
         */
        public final void signalAll() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            Node first = firstWaiter;
            if (first != null)
                doSignalAll(first);
        }

        /**
         * Implements uninterruptible condition wait.
         * <ol>
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * </ol>
         */
        public final void awaitUninterruptibly() {
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean interrupted = false;
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                if (Thread.interrupted())
                    interrupted = true;
            }
            if (acquireQueued(node, savedState) || interrupted)
                selfInterrupt();
        }

        /*
         * For interruptible waits, we need to track whether to throw
         * InterruptedException, if interrupted while blocked on
         * condition, versus reinterrupt current thread, if
         * interrupted while blocked waiting to re-acquire.
         */

        /**
         * Mode meaning to reinterrupt on exit from wait
         */
        private static final int REINTERRUPT = 1;
        /**
         * Mode meaning to throw InterruptedException on exit from wait
         */
        private static final int THROW_IE = -1;

        /**
         * Checks for interrupt, returning THROW_IE if interrupted
         * before signalled, REINTERRUPT if after signalled, or
         * 0 if not interrupted.
         */
        private int checkInterruptWhileWaiting(Node node) {
            return Thread.interrupted() ?
                    (transferAfterCancelledWait(node) ? THROW_IE : REINTERRUPT) :
                    0;
        }

        /**
         * Throws InterruptedException, reinterrupts current thread, or
         * does nothing, depending on mode.
         */
        private void reportInterruptAfterWait(int interruptMode)
                throws InterruptedException {
            if (interruptMode == THROW_IE)
                throw new InterruptedException();
            else if (interruptMode == REINTERRUPT)
                selfInterrupt();
        }

        /**
         * Implements interruptible condition wait.
         * <ol>
         * <li> If current thread is interrupted, throw InterruptedException.
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled or interrupted.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * <li> If interrupted while blocked in step 4, throw InterruptedException.
         * </ol>
         */
        // 线程入条件队列
        public final void await() throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            // 加入到条件队列的队尾
            Node node = addConditionWaiter();

            // 加入条件队列后，会释放 lock 时申请的资源，唤醒同步队列队列头的节点
            // 自己马上就要阻塞了，必须马上释放之前 lock 的资源，不然自己不被唤醒的话，别的线程永远得不到该共享资源了
            int savedState = fullyRelease(node);
            int interruptMode = 0;
            // 确认node不在同步队列上，再阻塞，如果 node 在同步队列上，是不能够上锁的
            // 目前想到的只有两种可能：
            // 1:node 刚被加入到条件队列中，立马就被其他线程 signal 转移到同步队列中去了
            // 2:线程之前在条件队列中沉睡，被唤醒后加入到同步队列中去
            while (!isOnSyncQueue(node)) {
                // this = AbstractQueuedSynchronizer$ConditionObject
                // 阻塞在条件队列上
                LockSupport.park(this);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            // 其他线程通过 signal 已经把 node 从条件队列中转移到同步队列中的数据结构中去了
            // 所以这里节点苏醒了，直接尝试 acquireQueued
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null) // clean up if cancelled
                // 如果状态不是CONDITION，就会自动删除
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
        }

        /**
         * Implements timed condition wait.
         * <ol>
         * <li> If current thread is interrupted, throw InterruptedException.
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled, interrupted, or timed out.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * <li> If interrupted while blocked in step 4, throw InterruptedException.
         * </ol>
         */
        public final long awaitNanos(long nanosTimeout)
                throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            final long deadline = System.nanoTime() + nanosTimeout;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    transferAfterCancelledWait(node);
                    break;
                }
                if (nanosTimeout >= spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                nanosTimeout = deadline - System.nanoTime();
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return deadline - System.nanoTime();
        }

        /**
         * Implements absolute timed condition wait.
         * <ol>
         * <li> If current thread is interrupted, throw InterruptedException.
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled, interrupted, or timed out.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * <li> If interrupted while blocked in step 4, throw InterruptedException.
         * <li> If timed out while blocked in step 4, return false, else true.
         * </ol>
         */
        public final boolean awaitUntil(Date deadline)
                throws InterruptedException {
            long abstime = deadline.getTime();
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (System.currentTimeMillis() > abstime) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                LockSupport.parkUntil(this, abstime);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }

        /**
         * Implements timed condition wait.
         * <ol>
         * <li> If current thread is interrupted, throw InterruptedException.
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled, interrupted, or timed out.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * <li> If interrupted while blocked in step 4, throw InterruptedException.
         * <li> If timed out while blocked in step 4, return false, else true.
         * </ol>
         */
        public final boolean await(long time, TimeUnit unit)
                throws InterruptedException {
            long nanosTimeout = unit.toNanos(time);
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            final long deadline = System.nanoTime() + nanosTimeout;
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                if (nanosTimeout >= spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                nanosTimeout = deadline - System.nanoTime();
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }

        //  support for instrumentation

        /**
         * Returns true if this condition was created by the given
         * synchronization object.
         *
         * @return {@code true} if owned
         */
        final boolean isOwnedBy(AbstractQueuedSynchronizer sync) {
            return sync == AbstractQueuedSynchronizer.this;
        }

        /**
         * Queries whether any threads are waiting on this condition.
         * Implements {@link AbstractQueuedSynchronizer#hasWaiters(ConditionObject)}.
         *
         * @return {@code true} if there are any waiting threads
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *                                      returns {@code false}
         */
        protected final boolean hasWaiters() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION)
                    return true;
            }
            return false;
        }

        /**
         * Returns an estimate of the number of threads waiting on
         * this condition.
         * Implements {@link AbstractQueuedSynchronizer#getWaitQueueLength(ConditionObject)}.
         *
         * @return the estimated number of waiting threads
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *                                      returns {@code false}
         */
        protected final int getWaitQueueLength() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            int n = 0;
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION)
                    ++n;
            }
            return n;
        }

        /**
         * Returns a collection containing those threads that may be
         * waiting on this Condition.
         * Implements {@link AbstractQueuedSynchronizer#getWaitingThreads(ConditionObject)}.
         *
         * @return the collection of threads
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *                                      returns {@code false}
         */
        protected final Collection<Thread> getWaitingThreads() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            ArrayList<Thread> list = new ArrayList<Thread>();
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION) {
                    Thread t = w.thread;
                    if (t != null)
                        list.add(t);
                }
            }
            return list;
        }
    }

    /**
     * Setup to support compareAndSet. We need to natively implement
     * this here: For the sake of permitting future enhancements, we
     * cannot explicitly subclass AtomicInteger, which would be
     * efficient and useful otherwise. So, as the lesser of evils, we
     * natively implement using hotspot intrinsics API. And while we
     * are at it, we do the same for other CASable fields (which could
     * otherwise be done with atomic field updaters).
     */
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("next"));

        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    /**
     * CAS head field. Used only by enq.
     */
    private final boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    /**
     * CAS tail field. Used only by enq.
     */
    private final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    /**
     * CAS waitStatus field of a node.
     */
    private static final boolean compareAndSetWaitStatus(Node node,
                                                         int expect,
                                                         int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset,
                expect, update);
    }

    /**
     * CAS next field of a node.
     */
    private static final boolean compareAndSetNext(Node node,
                                                   Node expect,
                                                   Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }
}
