应用1

Gift -> WarmGift ColdGift WildGift

GiftImpl -> Flower Ring Car

抽象类（Gift）的精确抽象（WarmGift） 里面组合了 实现类（GiftImpl）的 具体实现（Flower）


应用2
还有一个遥控和播放器的例子。两个都是接口，遥控是抽象类，播放器是实现类，播放器不包含遥控，但是遥控要控制播放器就要组合播放器。电视遥控组合电视，广播遥控组合广播
代码实现：
https://refactoringguru.cn/design-patterns/bridge/java/example#example-0--remotes-Remote-java
