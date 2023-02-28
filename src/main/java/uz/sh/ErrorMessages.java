package uz.sh;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author TheJWA (Jonibek)
 * @since 1/21/23/2:17 AM (Saturday)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {
    // Error messagelar shu yerda turishi kerak

    public static final String ROLE_NOT_FOUND = "ROLE NOT FOUND";
    public static final String ITEM_NOT_FOUND_WITH_THIS_ID = "Item not found with this id : ";
    public static final String ITEM_NOT_EXISTS_WITH_THIS_ID = "Item exists with this id : ";
    public static final String ROLE_EXISTS = "ROLE ALREADY EXISTS WITH GIVEN NAME";

    public static final String UNAUTHORIZED = "Please login first in order to access the resource! ";
    public static final String ACCESS_DENIED = "This resource is forbidden for this credentials! ";
    public static final String EXCEPTION_OCCURRED_WHILE_COMPRESSING_AND_SAVING_FILE_WITH_NAME = "Exception Occurred while compressing and saving this file with name : ";
    public static final String UNCOMPRESSIBLE_FILE_LOADED_WITH_NAME = "UnCompressible file tried to compress; Compressor for only .jpg, .jpeg, .png; FileName : ";
    public static final String EXCEPTION_OCCURRED_DOWNLOADING_FILE_WITH_ID = "Exception Occurred while downloading file with id :";
    public static final String EXCEPTION_OCCURRED_WHILE_DELETING_FILE_WITH_NAME = "Exception Occurred while deleting file with name : ";
    public static final String EXCEPTION_OCCURRED_WHILE_SAVING_FILE_WITH_NAME = "Exception Occurred while saving this file with name : ";
    public static final String CONFLICT_IN_LESSON_TIMES = "There is Time Conflicts with Group Lesson !!! Group name : ";
    public static final String THIS_STUDENT_ALREADY_STUDENT_IN_THIS_BRANCH = "This user already Student in this branch!";
    public static final String STUDENT_ALREADY_BIND_TO_THIS_GROUP = " Student already bind to this group ! Student id : ";
    public static final String STUDENT_IS_NOT_MEMBER_OF_THIS_GROUP = " This Student is not member of this group! Student id : ";
}
