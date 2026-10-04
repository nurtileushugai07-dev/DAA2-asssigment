import os
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv('results/results.csv')
os.makedirs('results/plots', exist_ok=True)

workloads = df['workload'].unique()

for w in workloads:
    sub_df = df[df['workload'] == w]
    variants = sub_df['variant'].unique()

    for v in variants:
        v_df = sub_df[sub_df['variant'] == v]
        if v_df.empty:
            continue

        title = f'Workload {w}' + (f' ({v})' if v != '-' else '')

        plt.figure(figsize=(8, 5))
        for struct in v_df['structure'].unique():
            s_data = v_df[v_df['structure'] == struct]
            plt.plot(s_data['n'], s_data['time_ms'], marker='o', label=struct)

        plt.title(f'{title} - Time vs n')
        plt.xlabel('n (Input Size)')
        plt.ylabel('Time (ms)')
        plt.xscale('log')
        plt.yscale('log')
        plt.grid(True, which="both", ls="--")
        plt.legend()
        plt.tight_layout()

        filename = f'results/plots/{w}_{v}_time.png'.replace('--', '_')
        plt.savefig(filename, dpi=300)
        plt.close()

        fig, axes = plt.subplots(1, 3, figsize=(18, 5))
        metrics = ['steps', 'moves', 'comparisons']

        for idx, metric in enumerate(metrics):
            ax = axes[idx]
            for struct in v_df['structure'].unique():
                s_data = v_df[v_df['structure'] == struct]
                ax.plot(s_data['n'], s_data[metric], marker='s', label=struct)
            ax.set_title(f'{metric.capitalize()} vs n')
            ax.set_xlabel('n')
            ax.set_ylabel(metric.capitalize())
            ax.set_xscale('log')
            ax.set_yscale('symlog', linthresh=1)
            ax.grid(True, which="both", ls="--")
            ax.legend()

        plt.suptitle(f'{title} - Metrics Breakdown')
        plt.tight_layout()

        filename_metrics = f'results/plots/{w}_{v}_metrics.png'.replace('--', '_')
        plt.savefig(filename_metrics, dpi=300)
        plt.close()

print('Result : results/plots/!')