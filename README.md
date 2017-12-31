# cityseletor

```java
class Main {
    public void init() {
        CitySelector
                        .init(MainActivity.this, R.id.parent)
                        .setCurrentItem("140000", "140200")
                        .addListener(new OnMoveListener() {
                            @Override
                            public void onMove(Bean province, Bean city, Bean district) {

                            }
                        })
                        .build();
}}
```
##注意：
1.澳门和香港没有District，所以需要在监听里做非空判断。
2.因为该控件是加了一层fragment，所以要传一个最外面的父布局