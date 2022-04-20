﻿using System.Threading.Tasks;
using Newtonsoft.Json;
using Other;
using UnityEngine;

namespace Models
{
    public static class JsonParse<T> where T : new()
    {
        public static async Task<T> GetDataFromPath(string jsonPath)
        {
            var jsonData = await Tools.LoadAssetAsync<TextAsset>(jsonPath);
            return JsonConvert.DeserializeObject<T>(jsonData.text);
        }
    }
    
    //数据表
    public class HangNumByScoreItem
    {
        public int id { get; set; }
        public int score_min { get; set; }
        public int hangNum { get; set; }
    }

    public class HangNumByScore
    {
        public HangNumByScoreItem[] data { get; set; }
    }
    
    public class LengthRateItem
    {
        public int id { get; set; }
        public int score_min { get; set; }
        public int rate_1 { get; set; }
        public int rate_2 { get; set; }
        public int rate_3 { get; set; }
        public int rate_4 { get; set; }
    }

    public class LengthRate
    {
        public LengthRateItem[] data { get; set; }
    }
    
    public class LevelItem
    {
        public int level { get; set; }
        public int score { get; set; }
        public string level_reward { get; set; }
        public int score_ice { get; set; }
        public int ice_1 { get; set; }
        public int ice_2 { get; set; }
        public int ice_3 { get; set; }
        public int hang_num { get; set; }
        public int delta_step { get; set; }
        public int max_ice_num { get; set; }
    }

    public class Level
    {
        public LevelItem[] data { get; set; }
    }
    
    public class SPItem
    {
        public int id { get; set; }
        public int bestScore_min { get; set; }
        public int bestScore_max { get; set; }
        public int curScore_min_1 { get; set; }
        public int curScore_max_1 { get; set; }
        public int rate_die_1 { get; set; }
        public int rate_notDie_1 { get; set; }
        public int curScore_min_2 { get; set; }
        public int curScore_max_2 { get; set; }
        public int rate_die_2 { get; set; }
        public int rate_notDie_2 { get; set; }
        public int curScore_min_3 { get; set; }
        public int curScore_max_3 { get; set; }
        public int rate_die_3 { get; set; }
        public int rate_notDie_3 { get; set; }
        public int curScore_min_4 { get; set; }
        public int curScore_max_4 { get; set; }
        public int rate_die_4 { get; set; }
        public int rate_notDie_4 { get; set; }
        public int curScore_min_5 { get; set; }
        public int curScore_max_5 { get; set; }
        public int rate_die_5 { get; set; }
        public int rate_notDie_5 { get; set; }
    }
    
    public class SP
    {
        public SPItem[] data { get; set; }
    }
    
    public class SPCItem
    {
        public int id { get; set; }
        public int combo_num { get; set; }
        public int rate_die { get; set; }
        public int rate_notDie { get; set; }
    }

    public class SPC
    {
        public SPCItem[] data { get; set; }
    }
    
    public class SPTItem
    {
        public int id { get; set; }
        public int day_1_bestScore { get; set; }
        public int day_1_time { get; set; }
        public int day_2_bestScore { get; set; }
        public int day_2_time { get; set; }
        public int day_3_bestScore { get; set; }
        public int day_3_time { get; set; }
    }

    public class SPT
    {
        public SPTItem[] data { get; set; }
    }
    
    public class UnitScoreItem
    {
        public int id { get; set; }
        public int score_min { get; set; }
        public int score_unit { get; set; }
    }

    public class UnitScore
    {
        public UnitScoreItem[] data { get; set; }
    }
    
    //成就相关
    public class AchievementComboNumberItem
    {
        public int id { get; set; }
        public int combo_num { get; set; }
        public int percent { get; set; }
    }

    public class AchievementComboNumber
    {
        public AchievementComboNumberItem[] data { get; set; }
    }
    
    public class AchievementDWItem
    {
        public int id { get; set; }
        public int dw_num { get; set; }
        public int percent { get; set; }
    }

    public class AchievementDW
    {
        public AchievementDWItem[] data { get; set; }
    }
    
    public class AchievementIceNumberItem
    {
        public int id { get; set; }
        public int ice_num { get; set; }
        public int percent { get; set; }
    }

    public class AchievementIceNumber
    {
        public AchievementIceNumberItem[] data { get; set; }
    }
    
    public class AchievementRemoveNumberItem
    {
        public int id { get; set; }
        public int remove_num { get; set; }
        public int percent { get; set; }
    }

    public class AchievementRemoveNumber
    {
        public AchievementRemoveNumberItem[] data { get; set; }
    }
    
    public class AchievementSkillMovesItem
    {
        public int id { get; set; }
        public int skill_num { get; set; }
        public int percent { get; set; }
    }

    public class AchievementSkillMoves
    {
        public AchievementSkillMovesItem[] data { get; set; }
    }
    
    public class AchievementStepScoreItem
    {
        public int id { get; set; }
        public int step_score_num { get; set; }
        public int percent { get; set; }
    }

    public class AchievementStepScore
    {
        public AchievementStepScoreItem[] data { get; set; }
    }
}
