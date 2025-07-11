package com.dd.ai_smart_course.sky;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

@SpringBootTest
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate() {
        System.out.println(redisTemplate);
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        HashOperations hashOperations = redisTemplate.opsForHash();
//        ListOperations listOperations = redisTemplate.opsForList();
//        SetOperations setOperations = redisTemplate.opsForSet();
//        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
    }

    /**
     * 操作字符串类型数据
     */
    @Test
    public void testString() {
        // set get setex setnx
        redisTemplate.opsForValue().set("city", "北京");
        String city = (String) redisTemplate.opsForValue().get("city");
        System.out.println( city);

        redisTemplate.opsForValue().set("code", "10086", 3, TimeUnit.MINUTES);
        redisTemplate.opsForValue().setIfAbsent("lock","1");
        redisTemplate.opsForValue().setIfAbsent("lock","2");
    }

    /**
     * 操作hash类型数据
     */
    @Test
    public void testHash() {
        // hset hget hdel hkeys hvals
        HashOperations hashOperations = redisTemplate.opsForHash();

        hashOperations.put("user1", "name", "张三");
        hashOperations.put("user1", "age", "18");

        String name = (String) hashOperations.get("user1", "name");
        System.out.println(name);

        Set keys = hashOperations.keys("user1");
        System.out.println(keys);

        List values = hashOperations.multiGet("user1", keys);
        System.out.println(values);

        hashOperations.delete("user1",  "age");
    }

    /**
     * 操作list类型数据
     */
    @Test
    public void testList() {
        //lpush lrange lpop llen
        ListOperations listOperations = redisTemplate.opsForList();

        listOperations.leftPushAll("list1", "a", "b", "c", "d");
        listOperations.leftPush("list1", "e");

        List myList = listOperations.range("list1", 0, -1);
        System.out.println(myList);

        listOperations.rightPop("list1");

        Long size = listOperations.size("list1");
        System.out.println(size);
    }

    /**
     * 操作set类型数据
     */
    @Test
    public void testSet() {
        //sadd smember scard sinter sunion srem
        SetOperations setOperations = redisTemplate.opsForSet();

        setOperations.add("set1", "a", "b", "c", "d");
        setOperations.add("set2", "a", "b", "e", "f");

        Set members = setOperations.members("set1");
        System.out.println(members);

        Long size = setOperations.size("set1");
        System.out.println(size);

        Set intersection = setOperations.intersect("set1", "set2");
        System.out.println(intersection);

        Set union = setOperations.union("set1", "set2");
        System.out.println(union);

        setOperations.remove("set1", "c", "b");
    }

    /**
     * 测试有序集合操作
     */
    @Test
    public void testZSet() {
        //zadd zrange zincrby zrem
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        zSetOperations.add("zset1", "a", 10);
        zSetOperations.add("zset1", "b", 12);
        zSetOperations.add("zset1", "c", 8);

        Set zset1 = zSetOperations.range("zset1", 0, -1);
        System.out.println(zset1);

        zSetOperations.incrementScore("zset1", "c", 10);

        zSetOperations.remove("zset1", "a", "b");

    }

    /**
     * 测试通用命令操作
     */
    @Test
    public void commonTest() {
        //keys exits type del
        Set keys = redisTemplate.keys("*");
        System.out.println(keys);

        boolean name = redisTemplate.hasKey("name");
        boolean set1 = redisTemplate.hasKey("set1");

        for (Object key : keys){
            DataType type = redisTemplate.type(key);
            System.out.println(type.name());
        }

        redisTemplate.delete("myList");
    }

    /**
     * 测试时间单位
     */
    @Test
    public void UnionTest() {
        System.out.println(TimeUnit.SECONDS.toMillis(1)); // 应该输出 1000
    }
}
