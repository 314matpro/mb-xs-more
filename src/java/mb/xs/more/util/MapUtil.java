package mb.xs.more.util;

import mb.xs.core.model.*;
import mb.xs.core.util.MapCreator;
import mb.xs.more.block.*;

public class MapUtil {
	private MapUtil() {
		/* Left intentionally blank */
	}

	public static MapCreator<Object, Object> create() {
		return create( Types.OBJECT_MODEL, Types.OBJECT_MODEL );
	}
	public static MapCreator<String, String> createStringMap() {
		return create( Types.STRING_MODEL, Types.STRING_MODEL );
	}
	public static MapCreator<Block, Block> createBlockMap() {
		return create( BlockUtil.MODEL, BlockUtil.MODEL );
	}
	public static <K, V> MapCreator<K, V> create( Model<K> keyType, Model<V> valueType ) {
		return new MapCreator<>( keyType, valueType );
	}
}
