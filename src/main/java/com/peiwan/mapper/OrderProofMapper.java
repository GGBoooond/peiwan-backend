package com.peiwan.mapper;

import com.peiwan.entity.OrderProof;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 工单凭证Mapper接口
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Mapper
public interface OrderProofMapper {

    /**
     * 插入工单凭证
     */
    @Insert("INSERT INTO order_proofs (order_id, proof_type, image_url, is_resubmission, " +
            "is_renewal, uploaded_at, created_at, updated_at, deleted) " +
            "VALUES (#{orderId}, #{proofType}, #{imageUrl}, #{isResubmission}, " +
            "#{isRenewal}, #{uploadedAt}, #{createdAt}, #{updatedAt}, #{deleted})")
    @org.apache.ibatis.annotations.Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderProof proof);

    /**
     * 根据ID查询工单凭证
     */
    @Select("SELECT * FROM order_proofs WHERE id = #{id} AND deleted = 0")
    OrderProof selectById(@Param("id") Long id);

    /**
     * 根据ID更新工单凭证
     */
    @Update("UPDATE order_proofs SET order_id = #{orderId}, proof_type = #{proofType}, " +
            "image_url = #{imageUrl}, is_resubmission = #{isResubmission}, " +
            "is_renewal = #{isRenewal}, uploaded_at = #{uploadedAt}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateById(OrderProof proof);

    /**
     * 根据ID删除工单凭证（逻辑删除）
     */
    @Update("UPDATE order_proofs SET deleted = 1, updated_at = NOW() WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 根据工单ID查找凭证
     */
    @Select("SELECT * FROM order_proofs WHERE order_id = #{orderId} AND deleted = 0 ORDER BY uploaded_at DESC")
    List<OrderProof> findByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据工单ID和凭证类型查找凭证
     */
    @Select("SELECT * FROM order_proofs WHERE order_id = #{orderId} AND proof_type = #{proofType} AND deleted = 0 ORDER BY uploaded_at DESC")
    List<OrderProof> findByOrderIdAndType(@Param("orderId") Long orderId, @Param("proofType") OrderProof.ProofType proofType);
}

