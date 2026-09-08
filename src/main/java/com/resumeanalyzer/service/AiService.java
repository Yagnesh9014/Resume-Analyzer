package com.resumeanalyzer.service;

import com.resumeanalyzer.exception.AiAnalysisException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String analyzeResume(String resumeText) {

        if (resumeText == null || resumeText.isBlank()) {
            throw new IllegalArgumentException(
                    "Resume text cannot be empty."
            );
        }

        String prompt = """
                You are an expert ATS (Applicant Tracking System) resume evaluator
                and professional career reviewer.

                Analyze the following resume carefully.

                Return the analysis in the EXACT structure below.

                Do not add any extra sections.
                Do not skip any section.
                Give realistic scores based only on the information present in the resume.

                === ATS SCORE ===

                Overall ATS Score: X/100

                Keyword Optimization: X/20
                ATS Formatting: X/20
                Technical Skills: X/20
                Experience & Projects: X/20
                Content Quality: X/20


                === PROFESSIONAL SUMMARY ===

                Write a concise professional summary based on the resume.


                === TECHNICAL SKILLS ===

                List the technical skills found in the resume.


                === EDUCATION ===

                Summarize the candidate's education.


                === PROJECTS ===

                List the important projects and briefly explain what each project demonstrates.


                === WORK EXPERIENCE ===

                Summarize the work experience.

                If no work experience is present, clearly state:
                "No professional work experience documented."


                === STRENGTHS ===

                Give 4-6 specific strengths supported by the resume.


                === MISSING OR WEAK SKILLS ===

                Identify important missing, weak, or insufficiently demonstrated skills.

                Do not claim a skill is missing if it is clearly present in the resume.


                === ATS ISSUES ===

                Identify resume formatting, structure, keyword, or ATS compatibility issues.

                If there are no major issues, state that clearly.


                === DETAILED REVIEW ===

                Give a detailed professional review of the resume.

                Explain:
                - What makes the resume strong
                - What could reduce ATS performance
                - What could reduce recruiter interest
                - Whether the projects effectively demonstrate the listed skills
                - Whether the resume is suitable for entry-level positions


                === IMPROVEMENT SUGGESTIONS ===

                Give 5-8 specific and actionable improvements.

                Each suggestion should explain what the candidate should change
                and why it would improve the resume.


                === RECOMMENDED JOB ROLES ===

                Recommend 5 suitable entry-level job roles based strictly on
                the candidate's education, skills, projects, and experience.


                === FINAL VERDICT ===

                Give a short final assessment of the resume and its readiness
                for applying to entry-level jobs.


                IMPORTANT RULES:

                1. Do not invent experience, skills, certifications, or achievements.
                2. Base the analysis only on the provided resume.
                3. Be honest and constructive.
                4. Keep the ATS score realistic.
                5. Do not give everyone a high score.
                6. Use clear language suitable for a job candidate.

                Resume:
                %s
                """.formatted(resumeText);

        try {

            String analysis = chatClient
                    .prompt(prompt)
                    .call()
                    .content();

            if (analysis == null || analysis.isBlank()) {
                throw new AiAnalysisException(
                        "AI analysis returned an empty response.",
                        null
                );
            }

            return analysis;

        } catch (AiAnalysisException e) {

            throw e;

        } catch (RuntimeException e) {

            throw new AiAnalysisException(
                    "AI service is temporarily unavailable.",
                    e
            );
        }
    }
}