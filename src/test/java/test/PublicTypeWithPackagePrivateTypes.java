package test;

/**
 *
 * @author Eric
 */
public class PublicTypeWithPackagePrivateTypes {
    public static class Nest{
        
    }
}
/** Uggg it pains me that you are allowed to do this*/
class APackagePrivateType{
    class Nest{
        
    }
}
/** Uggg it pains me that you are allowed to do this*/
interface APackagePrivateInterface{
    class Nest{
        
    }
}
/** Uggg it pains me that you are allowed to do this*/
@interface APackagePrivateAnnotation{
    class Nest{
        
    }
}
