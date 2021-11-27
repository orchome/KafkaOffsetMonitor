package www.orchome.com.kafka.core;


import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;

import java.util.Collection;
import java.util.List;

/**
 * <ul>
 *     <li>简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:</li>
 *     <li>1、持有Mapper的单例.</li>
 *     <li>2、泛型返回值转换.</li>
 *     <li>3、批量转换Collection中的所有对象.</li>
 *     <li>4、区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.</li>
 * </ul>
 */
public class BeanMapperUtil {

    /*** 持有Dozer单例, 避免重复创建DozerMapper消耗资源.***/
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * 基于Dozer转换对象的类型
     * @param obj 需要转换的对象
     * @param toObj 转换后的类型
     * @param <T> 返回对象类型泛型
     * @return 返回对象
     */
    public static <T> T objConvert(Object obj, Class<T> toObj) {
        if (null == obj) {
            return null;
        }
        return dozer.map(obj, toObj);
    }

    /**
     * 基于Dozer转换Collection中对象的类型
     * @param sourceList 需要转换的集合
     * @param toObj 转换后对象类型
     * @param <T> 返回对象类型泛型
     * @return 返回对象
     */
    public static <T> List<T> mapList(Collection<?> sourceList, Class<T> toObj) {
		if(null == sourceList){
			return null;
		}
        List<T> destinationList = Lists.newArrayList();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, toObj);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中
     * @param source 需要转换的对象
     * @param toObj 转换后对象类型
     */
    public static void copy(Object source, Object toObj) {
        if (null != source) {
            dozer.map(source, toObj);
        }
    }

}
