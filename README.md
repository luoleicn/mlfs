# MLFS

`MLFS` 是一个用 Java 实现的“从零开始理解机器学习”代码仓库。项目重点在于复现和理解经典统计学习算法，而不是追求工业级性能或最优指标。

仓库里的实现覆盖了若干典型任务，包括：

- 条件随机场（CRF）
- 最大熵文本分类（MaxEnt）
- 中文词性标注（HMM）
- 中文分词（基于 CRF）
- Voted Perceptron
- 基础矩阵分解推荐（SVD / MovieLens）
- 若干通用数值优化与核函数代码

项目原始说明位于 [src/readMe.txt](/private/tmp/mlfs/src/readMe.txt:1)。仓库现已补充标准 [LICENSE](/private/tmp/mlfs/LICENSE:1) 文件，主许可证按代码头注释采用 `GPL-3.0-or-later`。

## 目录结构

```text
.
├── corpus/                  # 仓库内附带的示例语料
├── src/mlfs/                # 主要源码
│   ├── crf/                 # 通用 CRF 实现
│   ├── textClassification/  # 最大熵文本分类示例
│   ├── pos/hmm/             # HMM 词性标注
│   ├── chineseSeg/          # 中文分词
│   ├── votedPerceptron/     # Voted Perceptron
│   ├── svd/                 # SVD 推荐模型
│   ├── movielens/           # MovieLens 示例入口
│   ├── svm/                 # SVM 相关代码（入口未完成）
│   └── numerical/ util/     # 数值优化与工具类
└── out/                     # 可选：编译输出目录
```

## 环境要求

- JDK 8 或更高版本
- 建议使用 UTF-8 编译源码

当前仓库已在 `javac 1.8.0_161` 下完成过一次全量编译验证。

## 编译

在项目根目录执行：

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name '*.java')
```

编译后可通过 `java -cp out <MainClass>` 运行各模块入口。

## Maven

仓库现已补充最小可用的 [pom.xml](/private/tmp/mlfs/pom.xml:1)，适配当前非标准目录结构（源码位于 `src/`）。

常用命令：

```bash
mvn compile
mvn package
```

如需在本地清理产物：

```bash
mvn clean
```

## 已附带的示例数据

仓库内当前包含以下语料：

- `corpus/baiduZhidao/`
- `corpus/chineseSegment/`
- `corpus/pos/`
- `corpus/votedperceptron/`

以下资源当前**不在仓库中**，运行对应示例前需要你自行准备：

- CRF/中文分词用的模板文件
- 训练后生成的 `.model` 文件
- `corpus/ml-100k/` MovieLens 数据

## 主要模块与运行方式

### 1. 通用 CRF

训练入口：`mlfs.crf.main.Train`

```bash
java -cp out mlfs.crf.main.Train <templateFile> <trainFile> <modelFile>
```

测试入口：`mlfs.crf.main.Test`

```bash
java -cp out mlfs.crf.main.Test <modelFile> <testFile> <outputFile>
java -cp out mlfs.crf.main.Test -n 5 <modelFile> <testFile> <outputFile>
```

说明：

- 支持 `n-best` 解码
- 训练使用 LBFGS
- 模板文件需要自行提供

### 2. 最大熵文本分类

训练入口：`mlfs.textClassification.maxent.main.Trainer`

```bash
java -cp out mlfs.textClassification.maxent.main.Trainer \
  corpus/baiduZhidao/zhidao_train.txt \
  maxent.model \
  100
```

测试入口：`mlfs.textClassification.maxent.main.Test`

```bash
java -cp out mlfs.textClassification.maxent.main.Test \
  maxent.model \
  corpus/baiduZhidao/zhidao_test.txt
```

说明：

- 代码里同时保留了 GIS 与 LBFGS 两种训练思路
- 示例训练入口默认走 `MELBFGS`

### 3. HMM 中文词性标注

训练入口：`mlfs.pos.hmm.main.Train`

```bash
java -cp out mlfs.pos.hmm.main.Train \
  corpus/pos/train.txt \
  lexicon.txt \
  ngrams.txt
```

测试入口：`mlfs.pos.hmm.main.Test`

```bash
java -cp out mlfs.pos.hmm.main.Test \
  corpus/pos/test.txt \
  lexicon.txt \
  ngrams.txt
```

说明：

- 使用 Viterbi 解码
- 使用线性插值平滑转移概率
- 包含未登录词处理逻辑
- 原项目补充说明见 [src/mlfs/pos/hmm/readme.txt](/private/tmp/mlfs/src/mlfs/pos/hmm/readme.txt:1)

### 4. 中文分词（CRF）

训练入口：`mlfs.chineseSeg.main.Train`

```bash
java -cp out mlfs.chineseSeg.main.Train <templateFile> <trainFile> <modelFile>
```

测试入口：`mlfs.chineseSeg.main.Test`

```bash
java -cp out mlfs.chineseSeg.main.Test
```

说明：

- `Train` 基于通用 CRF 训练分词模型
- [src/mlfs/chineseSeg/corpus/CorpusProcessing.java](/private/tmp/mlfs/src/mlfs/chineseSeg/corpus/CorpusProcessing.java:1) 可把原始语料转换成 CRF 训练格式
- `Test` 当前把模型路径写死为 `CRF.model`，并读取 `corpus/chineseSegment/pku_test.utf8`
- 如果你想让这个模块更易用，优先建议把 `Test` 改成命令行参数模式

### 5. Voted Perceptron

训练入口：`mlfs.votedPerceptron.main.Train`

```bash
java -cp out mlfs.votedPerceptron.main.Train
```

测试入口：`mlfs.votedPerceptron.main.Test`

```bash
java -cp out mlfs.votedPerceptron.main.Test
```

说明：

- 训练和测试文件路径当前写死为 `corpus/votedperceptron/a1a.txt` 与 `corpus/votedperceptron/a1a.t`
- 训练后模型默认输出为 `votedpercetpron.model`

### 6. SVD / MovieLens 示例

入口：`mlfs.movielens.Main`

```bash
java -cp out mlfs.movielens.Main
```

说明：

- 读取 `corpus/ml-100k/all.train` 和 `corpus/ml-100k/all.test`
- 当前仓库未附带 `ml-100k` 数据，因此该示例默认不能直接运行
- 训练后会生成 `basic_svd.model`

### 7. SVM

`mlfs.svm` 目录下已经有部分底层实现，但 [src/mlfs/svm/main/Train.java](/private/tmp/mlfs/src/mlfs/svm/main/Train.java:1) 目前是空入口，还不能作为完整示例使用。

## 当前仓库状态

从可运行性看，这个仓库更接近“算法学习代码集”而不是开箱即用的框架：

- 有完整实现，但不同模块的工程化程度不一致
- 一部分示例支持命令行参数
- 一部分示例把文件路径写死在代码里
- 没有 Maven / Gradle 构建文件
- 没有统一测试脚本

如果你准备继续维护，优先级建议如下：

1. 增加 `pom.xml` 或 `build.gradle`
2. 统一所有入口类的命令行参数
3. 补齐模板文件、模型示例和数据下载说明
4. 增加最小可复现实验脚本

## 快速开始

如果你只是想先跑通一个仓库内自带数据的模块，建议从这两个开始：

1. 最大熵文本分类：`corpus/baiduZhidao`
2. Voted Perceptron：`corpus/votedperceptron`

它们对外部资源依赖最少。
