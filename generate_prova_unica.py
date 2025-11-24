#!/usr/bin/env python3
import csv
from pathlib import Path
import subprocess
import textwrap

ROOT = Path(__file__).resolve().parent
CSV_PATH = ROOT / "alunos.csv"
OUTPUT_DIR = ROOT / "provas"
NOMINAL_TEX = OUTPUT_DIR / "nominais_unico.tex"
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)


HEADER = textwrap.dedent(r"""
\documentclass[12pt]{article}
\usepackage[utf8]{inputenc}
\usepackage[T1]{fontenc}
\usepackage{geometry}
\geometry{margin=2.5cm}
\usepackage{enumitem}
\usepackage{setspace}
\onehalfspacing

\begin{document}
""")
STUDENT_BLOCK_TEMPLATE = textwrap.dedent(r"""
\begin{center}
  {\Large \textbf{Prova Semestral de Testes de Software}}\\
  Centro Universitário Senac\\
  Professor: Afonso Brandão\\
  Disciplina: Testes de Software
\end{center}

\vspace{0.4cm}

\begin{center}
  \textbf{Aluno(a):} [[NAME]]
\end{center}

\vspace{0.5cm}

\begin{center}
  \textbf{Instruções:}
\end{center}
\begin{itemize}[leftmargin=*]
  \item Leia com atenção e responda de forma clara e organizada.
  \item A resposta deve ser redigida em texto corrido, destacando cada tópico estudado.
  \item Utilize exemplos ou referências às práticas realizadas ao longo do semestre.
\end{itemize}

\section*{Questão Única}
\textbf{Contexto:} o objetivo desta avaliação é permitir que você reflita sobre o conteúdo visto durante o semestre e demonstre a compreensão que construiu.

\textbf{Enunciado:} elabore um texto dissertativo no qual você apresente o que compreendeu ao longo do semestre de Testes de Software. \textit{Mostre todos os tópicos estudados}, agrupando-os em blocos temáticos ou por ordem cronológica das aulas, indicando a relevância de cada um para sua formação. Cite técnicas, ferramentas, tipos de teste, estratégias de automação, métricas e qualquer outra noção abordada em sala ou nas atividades práticas. Finalize comentando sobre como pode aplicar esse repertório em situações reais de desenvolvimento.

\textbf{Critérios de avaliação:}
\begin{enumerate}[label=\arabic*.]
  \item Compreensão dos tópicos e sua articulação em um texto coerente.
  \item Capacidade de citar exemplos e conectar teoria e prática.
  \item Organização e clareza da argumentação, incluindo a conclusão sobre aplicação futura.
\end{enumerate}
""")

FOOTER = "\\end{document}\n"


def load_names() -> list[str]:
    if not CSV_PATH.exists():
        raise FileNotFoundError(f"{CSV_PATH} não encontrado")
    with CSV_PATH.open(newline="", encoding="utf-8") as csvfile:
        reader = csv.DictReader(csvfile)
        return [row["nome"].strip() for row in reader if row.get("nome", "").strip()]


def build_document(names: list[str]) -> str:
    parts = [HEADER]
    for index, name in enumerate(names):
        if index:
            parts.append("\\newpage\n")
        block = STUDENT_BLOCK_TEMPLATE.replace("[[NAME]]", name)
        parts.append(block)
    parts.append(FOOTER)
    return "\n".join(parts)


def compile_document():
    cmd = [
        "pdflatex",
        "-interaction=nonstopmode",
        "-halt-on-error",
        "-output-directory",
        str(OUTPUT_DIR),
        "-jobname",
        "nominais_unico",
        str(NOMINAL_TEX),
    ]
    subprocess.run(cmd, check=True, cwd=ROOT)


def main() -> None:
    names = load_names()
    document = build_document(names)
    NOMINAL_TEX.write_text(document, encoding="utf-8")
    compile_document()


if __name__ == "__main__":
    main()
