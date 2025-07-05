package com.dd.ai_smart_course.dataset;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程知识图谱数据提取器
 * 从本地数据库提取课程、章节和概念信息，整合成知识图谱文档
 */
@Service
public class CourseKnowledgeExtractor {

    private static final String DB_URL = "jdbc:mysql://219.216.64.20:3306/ai_smart_course";
    private static final String USER = "root";
    private static final String PASS = "123456";

    /**
     * 提取所有知识图谱文档
     * @return 知识图谱文档列表
     */
    public List<Map<String, String>> extractKnowledgeDocuments() {
        List<Map<String, String>> documents = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // 1. 提取课程信息
            documents.addAll(extractCourseDocuments(conn));

            // 2. 提取章节信息
            documents.addAll(extractChapterDocuments(conn));

            // 3. 提取概念信息
            documents.addAll(extractConceptDocuments(conn));

            // 4. 提取概念关系信息
            documents.addAll(extractConceptRelationships(conn));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return documents;
    }

    /**
     * 提取课程文档
     */
    private List<Map<String, String>> extractCourseDocuments(Connection conn) throws SQLException {
        List<Map<String, String>> documents = new ArrayList<>();

        String sql = """
            SELECT c.id, c.name, c.description, c.credit, c.hours, c.status_self,
                   u.name as teacher_name
            FROM courses c
            LEFT JOIN users u ON c.teacher_id = u.id
            WHERE c.status_self = 'published'
            """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, String> doc = new HashMap<>();

                String courseName = rs.getString("name");
                String description = rs.getString("description");
                String teacherName = rs.getString("teacher_name");
                int credit = rs.getInt("credit");
                int hours = rs.getInt("hours");

                doc.put("EntityType", "Course");
                doc.put("EntityId", String.valueOf(rs.getInt("id")));
                doc.put("CourseName", courseName);
                doc.put("TeacherName", teacherName != null ? teacherName : "未指定");
                doc.put("DocumentType", "课程信息");
                doc.put("Description", description != null ? description : "");
                doc.put("Credit", String.valueOf(credit));
                doc.put("Hours", String.valueOf(hours));

                // 构建知识图谱内容
                String content = String.format(
                        "实体类型：课程\n" +
                                "课程名称：%s\n" +
                                "授课教师：%s\n" +
                                "课程描述：%s\n" +
                                "学分：%d\n" +
                                "学时：%d\n" +
                                "文档类型：课程信息",
                        courseName, teacherName != null ? teacherName : "未指定",
                        description != null ? description : "", credit, hours
                );

                doc.put("Content", content);
                documents.add(doc);
            }
        }

        return documents;
    }

    /**
     * 提取章节文档
     */
    private List<Map<String, String>> extractChapterDocuments(Connection conn) throws SQLException {
        List<Map<String, String>> documents = new ArrayList<>();

        String sql = """
            SELECT ch.id, ch.title, ch.content, ch.sequence,
                   c.name as course_name, c.id as course_id
            FROM chapters ch
            JOIN courses c ON ch.course_id = c.id
            WHERE c.status_self = 'published'
            ORDER BY c.id, ch.sequence
            """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, String> doc = new HashMap<>();

                String chapterTitle = rs.getString("title");
                String chapterContent = rs.getString("content");
                String courseName = rs.getString("course_name");
                int sequence = rs.getInt("sequence");

                doc.put("EntityType", "Chapter");
                doc.put("EntityId", String.valueOf(rs.getInt("id")));
                doc.put("CourseName", courseName);
                doc.put("CourseId", String.valueOf(rs.getInt("course_id")));
                doc.put("ChapterTitle", chapterTitle);
                doc.put("ChapterSequence", String.valueOf(sequence));
                doc.put("DocumentType", "章节信息");
                doc.put("Description", chapterContent != null ? chapterContent : "");

                // 构建知识图谱内容
                String content = String.format(
                        "实体类型：章节\n" +
                                "所属课程：%s\n" +
                                "章节标题：%s\n" +
                                "章节序号：%d\n" +
                                "章节内容：%s\n" +
                                "文档类型：章节信息",
                        courseName, chapterTitle, sequence,
                        chapterContent != null ? chapterContent : ""
                );

                doc.put("Content", content);
                documents.add(doc);
            }
        }

        return documents;
    }

    /**
     * 提取概念文档
     */
    private List<Map<String, String>> extractConceptDocuments(Connection conn) throws SQLException {
        List<Map<String, String>> documents = new ArrayList<>();

        String sql = """
            SELECT con.id, con.name, con.description, con.importance,
                   ch.title as chapter_title, ch.id as chapter_id,
                   c.name as course_name, c.id as course_id
            FROM concepts con
            JOIN chapters ch ON con.chapter_id = ch.id
            JOIN courses c ON ch.course_id = c.id
            WHERE c.status_self = 'published'
            ORDER BY c.id, ch.sequence, con.id
            """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, String> doc = new HashMap<>();

                String conceptName = rs.getString("name");
                String conceptDescription = rs.getString("description");
                String importance = rs.getString("importance");
                String chapterTitle = rs.getString("chapter_title");
                String courseName = rs.getString("course_name");

                doc.put("EntityType", "Concept");
                doc.put("EntityId", String.valueOf(rs.getInt("id")));
                doc.put("CourseName", courseName);
                doc.put("CourseId", String.valueOf(rs.getInt("course_id")));
                doc.put("ChapterTitle", chapterTitle);
                doc.put("ChapterId", String.valueOf(rs.getInt("chapter_id")));
                doc.put("ConceptName", conceptName);
                doc.put("Importance", importance);
                doc.put("DocumentType", "概念信息");
                doc.put("Description", conceptDescription != null ? conceptDescription : "");

                // 构建知识图谱内容
                String content = String.format(
                        "实体类型：概念\n" +
                                "所属课程：%s\n" +
                                "所属章节：%s\n" +
                                "概念名称：%s\n" +
                                "重要程度：%s\n" +
                                "概念描述：%s\n" +
                                "文档类型：概念信息",
                        courseName, chapterTitle, conceptName, importance,
                        conceptDescription != null ? conceptDescription : ""
                );

                doc.put("Content", content);
                documents.add(doc);
            }
        }

        return documents;
    }

    /**
     * 提取概念关系文档
     */
    private List<Map<String, String>> extractConceptRelationships(Connection conn) throws SQLException {
        List<Map<String, String>> documents = new ArrayList<>();

        // 基于章节的概念关系
        String sql = """
            SELECT DISTINCT 
                c1.id as concept1_id, c1.name as concept1_name,
                c2.id as concept2_id, c2.name as concept2_name,
                ch.title as chapter_title, ch.id as chapter_id, ch.sequence as sequence,
                course.name as course_name, course.id as course_id
            FROM concepts c1
            JOIN concepts c2 ON c1.chapter_id = c2.chapter_id AND c1.id != c2.id
            JOIN chapters ch ON c1.chapter_id = ch.id
            JOIN courses course ON ch.course_id = course.id
            WHERE course.status_self = 'published'
            AND c1.id < c2.id
            ORDER BY course.id, ch.sequence, c1.id, c2.id
            """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, String> doc = new HashMap<>();

                String concept1Name = rs.getString("concept1_name");
                String concept2Name = rs.getString("concept2_name");
                String chapterTitle = rs.getString("chapter_title");
                String courseName = rs.getString("course_name");
                int sequence = rs.getInt("sequence"); // 对应新增的字段

                doc.put("EntityType", "Relationship");
                doc.put("EntityId", rs.getInt("concept1_id") + "_" + rs.getInt("concept2_id"));
                doc.put("CourseName", courseName);
                doc.put("CourseId", String.valueOf(rs.getInt("course_id")));
                doc.put("ChapterTitle", chapterTitle);
                doc.put("ChapterId", String.valueOf(rs.getInt("chapter_id")));
                doc.put("Concept1Name", concept1Name);
                doc.put("Concept2Name", concept2Name);
                doc.put("RelationType", "同章节关联");
                doc.put("DocumentType", "概念关系");
                doc.put("Description", "同一章节内的概念关联关系");

                // 构建知识图谱内容
                String content = String.format(
                        "实体类型：关系\n" +
                                "所属课程：%s\n" +
                                "所属章节：%s\n" +
                                "关联概念1：%s\n" +
                                "关联概念2：%s\n" +
                                "关系类型：同章节关联\n" +
                                "关系描述：%s和%s都属于%s章节，存在逻辑关联\n" +
                                "文档类型：概念关系",
                        courseName, chapterTitle, concept1Name, concept2Name,
                        concept1Name, concept2Name, chapterTitle
                );

                doc.put("Content", content);
                documents.add(doc);
            }
        }

        return documents;
    }

    /**
     * 获取数据库统计信息
     */
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

            // 课程数量
            String courseSql = "SELECT COUNT(*) as count FROM courses WHERE status_self = 'published'";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(courseSql)) {
                if (rs.next()) {
                    stats.put("courses", rs.getInt("count"));
                }
            }

            // 章节数量
            String chapterSql = """
                SELECT COUNT(*) as count FROM chapters ch
                JOIN courses c ON ch.course_id = c.id
                WHERE c.status_self = 'published'
                """;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(chapterSql)) {
                if (rs.next()) {
                    stats.put("chapters", rs.getInt("count"));
                }
            }

            // 概念数量
            String conceptSql = """
                SELECT COUNT(*) as count FROM concepts con
                JOIN chapters ch ON con.chapter_id = ch.id
                JOIN courses c ON ch.course_id = c.id
                WHERE c.status_self = 'published'
                """;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(conceptSql)) {
                if (rs.next()) {
                    stats.put("concepts", rs.getInt("count"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        CourseKnowledgeExtractor extractor = new CourseKnowledgeExtractor();

        // 获取统计信息
        Map<String, Integer> stats = extractor.getStatistics();
        System.out.println("数据库统计信息：");
        System.out.println("课程数量：" + stats.get("courses"));
        System.out.println("章节数量：" + stats.get("chapters"));
        System.out.println("概念数量：" + stats.get("concepts"));

        // 提取知识文档
        List<Map<String, String>> documents = extractor.extractKnowledgeDocuments();
        System.out.println("\n提取的知识文档数量：" + documents.size());

        // 显示前几个文档示例
        int count = 0;
        for (Map<String, String> doc : documents) {
            if (count >= 3) break;
            System.out.println("\n文档 " + (count + 1) + "：");
            System.out.println("类型：" + doc.get("DocumentType"));
            System.out.println("内容：\n" + doc.get("Content"));
            count++;
        }
    }
}