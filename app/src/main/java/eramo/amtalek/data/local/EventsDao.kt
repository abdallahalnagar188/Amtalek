package eramo.amtalek.data.local

import androidx.room.*
import eramo.amtalek.data.local.entity.CartDataEntity

@Dao
interface EventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartData: CartDataEntity)

    @Query("SELECT EXISTS(SELECT * FROM CartDataEntity WHERE productIdFk = :productId)")
    suspend fun isProductExist(productId: String): Boolean

    @Query("UPDATE CartDataEntity SET productQty = productQty + 1 WHERE productIdFk=:productId")
    suspend fun updateQuantity(productId: String)

    @Query("SELECT * FROM CartDataEntity WHERE productIdFk=:productId")
    suspend fun getCartItemById(productId: String): CartDataEntity

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCartItem(cartData: CartDataEntity)

    @Query("DELETE FROM CartDataEntity WHERE id = :mainId")
    suspend fun removeCartItemById(mainId: Int)

    @Query("SELECT * FROM CartDataEntity")
    suspend fun getCartList(): List<CartDataEntity>

    @Query("DELETE FROM CartDataEntity")
    suspend fun clearCartList()

}