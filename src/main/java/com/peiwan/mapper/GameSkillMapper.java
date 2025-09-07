package com.peiwan.mapper;

import com.peiwan.entity.GameSkill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 游戏技能Mapper接口
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Mapper
public interface GameSkillMapper {

    /**
     * 插入游戏技能
     */
    @Insert("INSERT INTO game_skills (profile_id, game_name, play_style, service_type, " +
            "highest_rank, created_at, updated_at, deleted) " +
            "VALUES (#{profileId}, #{gameName}, #{playStyle}, #{serviceType}, " +
            "#{highestRank}, #{createdAt}, #{updatedAt}, #{deleted})")
    @org.apache.ibatis.annotations.Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(GameSkill skill);

    /**
     * 根据ID查询游戏技能
     */
    @Select("SELECT * FROM game_skills WHERE id = #{id} AND deleted = 0")
    GameSkill selectById(@Param("id") Long id);

    /**
     * 根据ID更新游戏技能
     */
    @Update("UPDATE game_skills SET profile_id = #{profileId}, game_name = #{gameName}, " +
            "play_style = #{playStyle}, service_type = #{serviceType}, highest_rank = #{highestRank}, " +
            "updated_at = #{updatedAt} WHERE id = #{id}")
    int updateById(GameSkill skill);

    /**
     * 根据ID删除游戏技能（逻辑删除）
     */
    @Update("UPDATE game_skills SET deleted = 1, updated_at = NOW() WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 根据员工资料ID查找游戏技能
     */
    @Select("SELECT * FROM game_skills WHERE profile_id = #{profileId} AND deleted = 0")
    List<GameSkill> findByProfileId(@Param("profileId") Long profileId);

    /**
     * 根据游戏名称查找技能
     */
    @Select("SELECT * FROM game_skills WHERE game_name = #{gameName} AND deleted = 0")
    List<GameSkill> findByGameName(@Param("gameName") String gameName);
}

