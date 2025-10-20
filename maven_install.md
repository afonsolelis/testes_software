```markdown
# Instalação do Apache Maven no PowerShell (Windows)

Este guia mostra como instalar o **Apache Maven** (`mvn`) no Windows usando o **PowerShell**, com suporte ao **Java 21**.

---

## ✅ Pré-requisitos

1. **Java 21 instalado**  
   Verifique com:
   ```powershell
   java -version
   ```

2. **Variável `JAVA_HOME` configurada**  
   Certifique-se de que aponta para o JDK 21:
   ```powershell
   echo $env:JAVA_HOME
   ```

---

## 🚀 Instalação via Scoop (recomendado)

Scoop é um gerenciador de pacotes para Windows que facilita a instalação de ferramentas CLI.

### Passo 1: Instalar o Scoop (se ainda não tiver)
```powershell
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
irm get.scoop.sh | iex
```

### Passo 2: Instalar o Maven
```powershell
scoop install maven
```

### Passo 3: Verificar instalação
```powershell
mvn -v
```

✅ Pronto! O Maven está instalado e disponível globalmente no PowerShell.

---

## 🔧 Instalação manual (alternativa)

### Passo 1: Baixar o Maven
Acesse [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi) e baixe o arquivo `.zip` (ex: `apache-maven-3.9.9-bin.zip`).

### Passo 2: Extrair o arquivo
Extraia para uma pasta permanente, como:
```
C:\apache-maven-3.9.9
```

### Passo 3: Adicionar ao PATH
Execute no PowerShell **como Administrador**:
```powershell
[Environment]::SetEnvironmentVariable(
    "Path",
    [Environment]::GetEnvironmentVariable("Path", [EnvironmentVariableTarget]::Machine) + ";C:\apache-maven-3.9.9\bin",
    [EnvironmentVariableTarget]::Machine
)
```

> ⚠️ Substitua o caminho conforme sua instalação.

### Passo 4: Reinicie o PowerShell e teste
```powershell
mvn -v
```

---

## ✅ Saída esperada
```text
Apache Maven 3.9.9 (...)
Maven home: C:\apache-maven-3.9.9
Java version: 21.0.2, vendor: Eclipse Adoptium, runtime: C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot
Default locale: pt_BR, platform encoding: Cp1252
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

Se a versão do Java for **21.x**, está tudo configurado corretamente! 🎉
```