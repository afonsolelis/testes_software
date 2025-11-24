#!/usr/bin/env python3
import csv
import re
import subprocess
import unicodedata
from pathlib import Path

ROOT = Path(__file__).resolve().parent
CSV_PATH = ROOT / "alunos.csv"
TEX_PATH = ROOT / "provas" / "prova.tex"
OUTPUT_DIR = ROOT / "provas" / "nominais"
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)


def slugify(name: str) -> str:
    normalized = unicodedata.normalize("NFKD", name)
    ascii_str = normalized.encode("ascii", errors="ignore").decode("ascii")
    slug = re.sub(r"[^0-9a-zA-Z]+", "_", ascii_str).strip("_")
    return slug.lower() or "aluno"


def main() -> None:
    if not CSV_PATH.exists():
        raise FileNotFoundError(f"{CSV_PATH} não encontrado")

    with CSV_PATH.open(newline="", encoding="utf-8") as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            nome = row.get("nome", "").strip()
            if not nome:
                continue

            slug = slugify(nome)
            jobname = f"prova_{slug}"
            cmd = [
                "pdflatex",
                "-interaction=nonstopmode",
                "-halt-on-error",
                "-output-directory",
                str(OUTPUT_DIR),
                "-jobname",
                jobname,
                f"\\def\\AlunoNome{{{nome}}}\\input{{{TEX_PATH}}}",
            ]

            print(f"Gerando prova nominal para {nome} -> {jobname}.pdf")
            subprocess.run(cmd, check=True, cwd=ROOT)


if __name__ == "__main__":
    main()
