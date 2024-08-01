package com.dan_michael.example.demo.util;

public final class Constants {
//    Authentication
    public static final String User_Not_Found = "User Not Found In Database, Please Check Email Again";
    public static final String Email_ARD_Exist = "This Email Has Been Registered, Please Enter Another Email !!!";

//  Account Response
    public static final String Verification_Success = "Account Verification Successful";
    public static final String Verification_Fail = "Account Verification Failed !!!";
    public static final String Password_Not_Match = "RePassword Are Not The Same. It Does Not Match !!!";
    public static final String Change_FGPassword_Success = "Update Forget Password Successfully ";
    public static final String Change_InformationPF_Success = "Update Information Profile User Successfully";
    public static final String Change_InformationPF_Fail = "Update Forget Password Successfully";
    public static final String Wrong_Password = "Wrong Password, The Password You Entered Does Not Match The Saved Password.!!!";
    public static final String Change_Password = "Update Password User Successfully ";
//  Comment Response
    public static final String Update_Comment_Fail = "Update Comment Failed";
    public static final String Update_Comment_Success = "Update Comment Successfully";
    public static final String Delete_Comment_Fail = "Delete Comment Failed";
    public static final String Delete_Comment_Success = "Delete Comment Successfully";
    public static final String Comment_Not_Found = "Error! Comment Do Not Exist, Comment Not Found In Database !!!";
    public static final String Comment_Permission_Fail = "Error! You Sre Not S Commenter On This Comment, Edit Permission Failed !!!";

//  Product Response
    public static final String Product_ARD_Exist = "Product Already Exists !!!";
    public static final String Product_Update_Fail = "Error, Update Product Failed !!!";
    public static final String Color_Detail_Not_Found = "Color Including List Of Quantity And Size Details Information Not Found In Database, Please Check Product Name or Color Again";
    public static final String Delete_Product_Fail = "Delete Product Information Failed, Product Not Found !!!";
    public static final String Delete_Product_Success = "Delete Product Information Successfully";
    public static final String List_Color_Detail_Not_Found = "Quantity Total Including Color And List Of Quantity And Size Details Information Not Found In Database, Please Check Product Name or Color Again";
    public static final String Fetch_Data_Colors_QuantityDetail_Fail = "Fetch Data Color Including List Of Quantity And Size Details Information Failed";
    public static final String Fetch_Data_Colors_QuantityDetail_Success = "Fetch Data Color Including List Of Quantity And Size Details Information Successfully";
    public static final String Fetch_Data_Quantity_Total_Fail = "Fetch Data Quantity Total Color And Including List Of Quantity And Size Details Information Failed";
    public static final String Fetch_Data_Quantity_Total_Success = "Fetch Data Quantity Total Including Color And List Of Quantity And Size Details Information Successfully";
    public static final String Add_Favorite_Success = "Add Favorite product Successfully";
    public static final String Add_Favorite_Fail =  "Add Favorite product Failure !!!";
    public static final String Delete_Favorite_Success = "Delete Favorite product Successfully";
    public static final String Delete_Favorite_Fail =  "Delete Favorite product Failure !!!";

//  Category - Brand response
    public static final String Category_ARD_Exist = "Category Already Exists !!!";
    public static final String Fetch_Data_Brand_Fail = "Fetch Data Brand Information Failed";
    public static final String Fetch_Data_Brand_Success = "Fetch Data Brand Information Successfully";

    public static final String Delete_Brand_Fail = "Delete Brand Information Failed";
    public static final String Delete_Brand_Success = "Delete Brand Information Successfully";

    public static final String Delete_Category_Fail = "Delete Category Information Failed";
    public static final String Delete_Category_Success = "Delete Category Information Successfully";
//  Payment Methods response
    public static final String Payment_Methods_ARD_Exist = "Payment Methods Already Exists !!!";
    public static final String Delete_Payment_Methods_Success = "Delete Payment Methods Successfully";
    public static final String Delete_Payment_Methods_Fail = "Delete Payment Methods Failure !!";

//  Cart response

    public static final String Add_Cart_Success = "Add Product To Cart Successfully";
    public static final String Add_Cart_Fail = "Add Product To Cart Failed";
    public static final String Update_Cart_Success = "Update Information Item Detail Of Cart Successfully";
    public static final String Update_Cart_Fail = "Update Information Item Detail Of Cart  Failed";
    public static final String Cart_Empty = "Cart Does Not Exist Or User Has Not Added Products To Cart";
    public static final String Make_Empty_Cart_Success = "Make Empty Cart Successfully";
    public static final String Make_Empty_Cart_Fail =  "Make Empty Cart Failure !!!";
    public static final String Delete_Cart_Item_Success = "Delete Cart Item Successfully";
    public static final String Delete_Cart_Item_Fail =  "Delete Cart Item Failure !!!";

//  Order response
    public static final String Delete_Order_Success = "Delete Order Successfully";
    public static final String Delete_Order_Fail =  "Delete Order Failure !!!";
    public static final String Delete_Order_Detail_Success = "Delete Order Detail Successfully";
    public static final String Delete_Order_Detail_Fail =  "Delete Order Detail Failure !!!";
//  Order Shipping Status
    public static final String Order_Status_Wait =  "Chờ Lấy Hàng";
    public static final String Order_Status_Take_Process_ =  "Đang Đi Lấy";
    public static final String Order_Status_Delivery_Process =  "Đang Giao Hàng";
    public static final String Order_Status_Delivered =  "Đã Giao Hàng";
    public static final String Order_Status_Refund =  "Chuyển hoàn";
    public static final String Order_Status_No_Customer_Contact =  "Không gặp khách";
    public static final String Order_Status_Unfinished =  "Chưa hoàn thành";
//  VNPay response
    public static final String VnPayStatus_Success =  "Success";
    public static final String VnPayStatus_Fail =  "Failed";
    public static final String VnPay_Code_00=  "00";
//  ChatBot Email response
    public static final String Create_QuestionAnswer_For_Guest_Fail =  "Created A Response To The Customer Failed And Sent an Email To The Customer Unsuccessfully";
    public static final String Create_QuestionAnswer_For_Guest_Success =  "Successfully created A Response For The Customer And Sent An Email To The Customer.";
    public static final String Create_Question_For_Guest_Fail =  "Create Successful Questions For Customers Failure, Please Check Information Of Your Email";
    public static final String Create_Question_For_Guest_Success =  "Create Successful Questions For Customers Successfully, Please Wait For A Reply From Customer Service Soon.";
    public static final String Answer_Exists =  "The question has been answered by the administrator, you can ask Chatbot for help on this issue.";
    public static final String Question_Does_Not_Exists =  "Question does not exist, answer update failed !";

    public static final String Question_Exists =  "The question already exists in the repository, you can ask Chatbot for help with this issue.";
    public static final String Logo_Path =  "D:\\Downloads\\PJ_CDTN\\demo\\src\\main\\resources\\img.png";
    public static final String Subject_Answer_Question =  "Thank You for Your Question - Our Response Inside!";

}