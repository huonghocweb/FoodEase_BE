package poly.foodease.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private final String secretKey  = "huongphamhuongphamhuongphamhuongphamhuongphamhuongpham";
    public String extractUsername(String token){
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }
    public Date extractExpiration(String token){
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();

    }
    public List<String> extractRoles(String token){
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }
    /* hoặc cách sử dụng extracClaim
    private String extractUserName(String token){
        // Claims::getSubject == <Claims,T>
        return extractClaim(token, Claims :: getSubject);
    }
        <T> : kiểu dữ liệu tổng quát , cho phép trả về bất cứ kiểu nào
         Claims : Nội dung được mã hóa trong phần thân (payLoad) của token
         Function<Claims ,T> claimsResolver) : 1 hàm nhận vào đối tượng Claims , trả về giá trị kiểu T
         return extractClaim(token, Claims:: getExpiration);
     */
    private <T> T extractClaim(String token, Function<Claims , T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        // khi apply claims vào claimsResoverl , thì ta có thể lấy những thông tin cụ thể từ claimsResolver khi cần thiết
        // giống như lấy giá trị key,value trong map .
        return claimsResolver.apply(claims);
    }

    // Truyền vào token , cung cấp secretKey để Jwts kiểm tra và lấy ra phần Body(Payload) của token
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // Kiểm tra token đã hết hạn chưa so với thời gian hiện tại
    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public String generateToken(UserDetails userDetails){
        List<String > roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority:: getAuthority)
                .collect(Collectors.toList());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles",roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *10))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}