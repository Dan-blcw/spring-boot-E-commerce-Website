package com.dan_michael.example.demo.util;

import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;
import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.model.response.ProductResponse;

import java.util.List;

public class UtilFunction {
    public static String saveInfoChatBotAnswer(ProductResponse qa, List<String> sizes, List<String> colors, List<SubColor> boxResponse) {
        String saveSizes = String.join(", ", sizes);
        String saveColors = String.join(", ", colors);

        StringBuilder saveDetail = new StringBuilder();
        for (var x : boxResponse) {
            saveDetail.append("màu ").append(x.getColor());
            for (var y : x.getSizes()) {
                saveDetail.append(", ").append(" size ").append(y.size).append(" số lượng ").append(y.quantity);
            }
            saveDetail.append(". ");
        }

        return "SẢN PHẨM: " + qa.getName() +
                " thuộc danh mục " + qa.getCategory() +
                " - " + qa.getSubCategory() +
                ". Màu sắc: " + saveColors +
                ", Kích thước: " + saveSizes +
                ". Chi tiết số lượng: " + saveDetail +
                " Tổng: " + qa.getTotalQuantity() + " sản phẩm." +
                " Giá gốc: " + qa.getOriginalPrice() + " VND, giảm " + qa.getSaleDiscountPercent() +
                "%, còn " + qa.getFinalPrice() + " VND." +
                " Đánh giá: " + qa.getRating() + " ✨.";
    }
    public static String saveInfoChatBotAnswer(Product qa, List<String> sizes, List<String> colors, List<SubColor> boxResponse) {
        String saveSizes = String.join(", ", sizes);
        String saveColors = String.join(", ", colors);

        StringBuilder saveDetail = new StringBuilder();
        for (var x : boxResponse) {
            saveDetail.append("màu ").append(x.getColor());
            for (var y : x.getSizes()) {
                saveDetail.append(", ").append(" size ").append(y.size).append(" số lượng ").append(y.quantity);
            }
            saveDetail.append(". ");
        }

        return "SẢN PHẨM: " + qa.getName() +
                " thuộc danh mục " + qa.getCategory() +
                " - " + qa.getSubCategory() +
                ". Màu sắc: " + saveColors +
                ", Kích thước: " + saveSizes +
                ". Chi tiết số lượng: " + saveDetail +
                " Tổng: " + qa.getTotalQuantity() + " sản phẩm." +
                " Giá gốc: " + qa.getOriginalPrice() + " VND, giảm " + qa.getSaleDiscountPercent() +
                "%, còn " + qa.getFinalPrice() + " VND." +
                " Đánh giá: " + qa.getRating() + " ✨.";
    }
    public static String savesizesAnswer(String namepro, List<String> sizes){
        String save = "";
        String saveSizes = "";
        for (var x =0 ;x < sizes.size() ; ++x) {
            if(x < sizes.size()){
                saveSizes +=  sizes.get(x) + ", ";
            } else if (x == sizes.size()) {
                saveSizes +=  sizes.get(x);
            }
        }
        return save + " Hiện Tại có " + sizes.size() + " kích thước .Danh sách gồm : [" +saveSizes+"]. Để biết thông tin chi biết về số lương và kích thước chi tiết bạn có thể đặt câu hỏi : Số lượng chi tiết của "+namepro;
    }
    public static String savecolorsAnswer(String namepro,List<String> colors){
        String save = namepro ;
        String savecolors = "";
        for (var x =0 ;x < colors.size() ; ++x) {
            if(x < colors.size()){
                savecolors +=  colors.get(x) + ", ";
            } else if (x == colors.size()) {
                savecolors +=  colors.get(x);
            }
        }
        return save + " Hiện Tại có " + colors.size() + " màu .Danh sách gồm : [" +savecolors+"]. Để biết thông tin chi biết về số lương và màu chi tiết bạn có thể đặt câu hỏi : Chi tiết Quantity Detail of "+namepro;
    }
    public static String saveQuantityDetailAnswer(ProductResponse qa, List<String> sizes, List<String> colors, List<SubColor> boxResponse){
        String saveDetail = "Số lượng chi tiết của " + qa.getName() + "\n";
        for (var x: boxResponse){
            saveDetail +=  "Màu " + x.getColor().toUpperCase();
            for(var y: x.getSizes()){
                saveDetail += " có số lượng "+ y.quantity+ " cho kích thước "+ y.size.toUpperCase() +".\n";
            }
        }
        return saveDetail + ". Nếu bạn muốn yêu cầu màu hoặc thích thước khác thì bạn có thể gửi cầu hỏi cho quản trị viên để có thể nhận được phản hồi sớm !";
    }

    public static String saveQuantityDetailAnswer(String qa, List<String> sizes, List<String> colors, List<SubColor> boxResponse){
        String saveDetail = "Số lượng chi tiết của " + qa + "\n";
        for (var x: boxResponse){
            saveDetail +=  "Màu " + x.getColor().toUpperCase();
            for(var y: x.getSizes()){
                saveDetail += " có số lượng "+ y.quantity+ " cho kích thước "+ y.size.toUpperCase() +".\n";
            }
        }
        return saveDetail + ". Nếu bạn muốn yêu cầu màu hoặc thích thước khác thì bạn có thể gửi cầu hỏi cho quản trị viên để có thể nhận được phản hồi sớm !";
    }

//    public static String rating(String namepro,Float rating, Integer nRating){
//        if(rating >= 4){
//            return "Sản phẩm "+ namepro + " hiện đang được đánh giá tôt nhất với rating là " + rating +" ✨ với tổng "+ nRating+ " đánh giá từ khách hàng. Tôi nghĩ sản phẩm này là 1 trong những lựa chọn tốt cho bạn";
//        }else if(rating >= 3){
//            return "Sản phẩm "+ namepro + " hiện đang được đánh giá ổn với rating là " + rating +" \uD83C\uDF1F với tổng "+ nRating+ " đánh giá từ khách hàng. Đây có thể sẽ là sản phẩm phù với bạn hoặc bạn có thể tìm kiếm các sản phẩm tương tự tốt hơn";
//        }else {
//            return "Sản phẩm" + namepro + " hiện đang được đánh giá không được tốt với rating là " + rating +" ⭐ với tổng "+ nRating+" đánh giá từ khách hàng. Sản phẩm đang được hoàn thiện và cải tiến để có thể đáp ứng tốt hơn khách hang \n.Nếu bạn thấy sản phẩm phù hợp với bản thân thi có thể thêm vào giở hàng và tiến hành order";
//        }
//    }
    public static String rating(String namepro, Float rating, Integer nRating) {
        if (rating >= 4) {
            return "Sản phẩm " + namepro + " hiện đang được đánh giá tốt nhất với rating là " + rating + " ✨ với tổng " + nRating + " đánh giá từ khách hàng. Tôi nghĩ sản phẩm này là 1 trong những lựa chọn tốt cho bạn.";
        } else if (rating >= 3) {
            return "Sản phẩm " + namepro + " hiện đang được đánh giá ổn với rating là " + rating + " \uD83C\uDF1F với tổng " + nRating + " đánh giá từ khách hàng. Đây có thể sẽ là sản phẩm phù hợp với bạn hoặc bạn có thể tìm kiếm các sản phẩm tương tự tốt hơn.";
        } else if (rating >= 2) {
            return "Sản phẩm " + namepro + " hiện đang được đánh giá khá thấp với rating là " + rating + " ⭐⭐ với tổng " + nRating + " đánh giá. Có thể cần cân nhắc kỹ trước khi mua hoặc tìm kiếm sản phẩm khác phù hợp hơn.";
        } else if (rating >= 1) {
            return "Sản phẩm " + namepro + " hiện đang được đánh giá rất thấp với rating là " + rating + " ⭐ với tổng " + nRating + " đánh giá. Sản phẩm có nhiều phản hồi chưa tốt, bạn nên xem xét kỹ.";
        } else {
            return "Sản phẩm " + namepro + " hiện đang được đánh giá cực kỳ kém với rating là " + rating + " ⭐ với tổng " + nRating + " đánh giá. Đây có thể không phải là lựa chọn tốt cho bạn, cần cân nhắc trước khi mua.";
        }
    }
}
